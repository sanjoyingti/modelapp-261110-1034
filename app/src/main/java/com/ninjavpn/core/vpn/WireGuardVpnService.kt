package com.ninjavpn.core.vpn

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import com.wireguard.android.backend.GoBackend
import com.wireguard.config.Config
import com.wireguard.config.InterfaceConfig
import com.wireguard.config.PeerConfig
import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.model.Protocol
import timber.log.Timber

class WireGuardVpnService : VpnService() {

    private var backend: GoBackend? = null
    private var currentServer: Server? = null

    override fun onCreate() {
        super.onCreate()
        backend = GoBackend.getInstance(this)
        // Ensure Go backend is initialized
        backend?.start(this)
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

    private fun connect(server: Server) {
        currentServer = server
        val config = buildWireGuardConfig(server)
        backend?.setConfig(config, object : GoBackend.Callback {
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
    }

    private fun buildWireGuardConfig(server: Server): Config {
        val iface = InterfaceConfig.Builder()
            .privateKey(KeyGenerator.generatePrivateKey()) // persisted per install
            .addresses(server.allowedIPs)
            .dns(server.dns)
            .mtu(server.mtU)
            .build()

        val peer = PeerConfig.Builder()
            .publicKey(server.publicKey.decodeHex())
            .endpoint("${server.hostname}:${server.port}")
            .allowedIPs(server.allowedIPs)
            .persistentKeepalive(server.keepAlive)
            .build()

        return Config.Builder()
            .setInterface(iface)
            .addPeer(peer)
            .build()
    }

    private fun disconnect() {
        backend?.setConfig(null, object : GoBackend.Callback {
            override fun onStateChange(state: GoBackend.State) {
                if (state == GoBackend.State.DISCONNECTED) {
                    stopForeground(true)
                    stopSelf()
                }
            }
            override fun onError(error: String) { /* ignore */ }
        })
    }

    override fun onDestroy() {
        backend?.stop()
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