package com.ninjavpn.core.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.wireguard.android.backend.GoBackend
import com.wireguard.config.Config
import com.wireguard.config.InterfaceConfig
import com.wireguard.config.PeerConfig
import com.ninjavpn.domain.model.Server
import com.ninjavpn.ui.MainActivity
import timber.log.Timber
import java.net.InetAddress

class WireGuardVpnService : VpnService() {

    private var backend: GoBackend? = null
    private var currentServer: Server? = null
    private var vpnFd: ParcelFileDescriptor? = null

    override fun onCreate() {
        super.onCreate()
        backend = GoBackend.getInstance()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val server = intent?.getParcelableExtra<Server>(EXTRA_SERVER)
        val action = intent?.action

        return when (action) {
            ACTION_CONNECT -> {
                server?.let { connect(it) }
                START_STICKY
            }
            ACTION_DISCONNECT -> {
                disconnect()
                START_NOT_STICKY
            }
            else -> START_STICKY
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "VPN Connection",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows VPN connection status"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(serverName: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VPN Connected")
            .setContentText("Connected to $serverName")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    private fun connect(server: Server) {
        currentServer = server
        showNotification(server.name)
        
        try {
            val config = buildWireGuardConfig(server)
            val builder = Builder()
                .setSession("NinjaVPN")
                .addAddress(InetAddress.getByName("10.0.0.2"), 24)
                .addRoute("0.0.0.0", 0)
                .addDnsServer(InetAddress.getByName("1.1.1.1"))
                .setMtu(server.mtU)
                .setBlocking(true)

            vpnFd = builder.establish()
            
            backend?.setState(config, object : GoBackend.Callback {
                override fun onStateChange(state: GoBackend.State) {
                    Timber.d("WireGuard state: $state")
                    when (state) {
                        GoBackend.State.CONNECTED -> notifyConnected()
                        GoBackend.State.DISCONNECTED -> notifyDisconnected()
                        GoBackend.State.FAILED -> notifyError("Connection failed")
                    }
                }

                override fun onError(error: String) {
                    Timber.e("WireGuard error: $error")
                    notifyError(error)
                }
            })
        } catch (e: Exception) {
            Timber.e(e, "Failed to connect VPN")
            notifyError(e.message ?: "Unknown error")
        }
    }

    private fun buildWireGuardConfig(server: Server): Config {
        val privateKey = KeyGenerator.generatePrivateKey()
        
        val iface = InterfaceConfig.Builder()
            .privateKey(privateKey)
            .addAddress(com.wireguard.ipnet.InetNetwork.parse("10.0.0.2/24"))
            .addDnsServer(com.wireguard.ipnet.InetAddress.parse("1.1.1.1"))
            .mtu(server.mtU)
            .build()

        val peer = PeerConfig.Builder()
            .publicKey(com.wireguard.crypto.Key.parse(server.publicKey))
            .endpoint(com.wireguard.util.Endpoint.parse("${server.hostname}:${server.port}"))
            .addAllowedIp(com.wireguard.ipnet.InetNetwork.parse("0.0.0.0/0"))
            .persistentKeepalive(server.keepAlive)
            .build()

        return Config.Builder()
            .setName("NinjaVPN")
            .addInterface(iface)
            .addPeer(peer)
            .build()
    }

    private fun disconnect() {
        try {
            backend?.setState(null, null)
            vpnFd?.close()
            vpnFd = null
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Timber.e(e, "Error disconnecting VPN")
        }
    }

    private fun notifyConnected() {
        currentServer?.let { server ->
            VpnManager.getInstance()?.onVpnStateChanged(
                com.ninjavpn.domain.model.VpnState.Connected(
                    server,
                    com.ninjavpn.domain.model.TrafficStats()
                )
            )
        }
    }

    private fun notifyDisconnected() {
        VpnManager.getInstance()?.onVpnStateChanged(
            com.ninjavpn.domain.model.VpnState.Disconnected(null)
        )
    }

    private fun notifyError(message: String) {
        VpnManager.getInstance()?.onVpnStateChanged(
            com.ninjavpn.domain.model.VpnState.Error(message, true)
        )
    }

    override fun onDestroy() {
        disconnect()
        backend = null
        super.onDestroy()
    }

    companion object {
        const val ACTION_CONNECT = "com.ninjavpn.action.CONNECT"
        const val ACTION_DISCONNECT = "com.ninjavpn.action.DISCONNECT"
        const val EXTRA_SERVER = "server"
        const val CHANNEL_ID = "vpn_channel"
    }
}
