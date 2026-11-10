package com.ninjavpn.core.vpn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.model.VpnState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VpnManager @Inject constructor(
    private val context: Context,
) {
    private val _state = MutableStateFlow<VpnState>(VpnState.Disconnected(null))
    val state: StateFlow<VpnState> = _state

    private var pendingIntent: Intent? = null

    fun prepareVpn(activity: Activity, requestCode: Int): Boolean {
        val intent = VpnService.prepare(context)
        if (intent != null) {
            pendingIntent = intent
            activity.startActivityForResult(intent, requestCode)
            return false
        }
        return true
    }

    fun onActivityResult(requestCode: Int, resultCode: Int): Boolean {
        if (requestCode == REQUEST_VPN_PERMISSION) {
            return resultCode == Activity.RESULT_OK
        }
        return false
    }

    fun connect(server: Server) {
        _state.value = VpnState.Connecting(server)
        val intent = Intent(context, WireGuardVpnService::class.java).apply {
            action = WireGuardVpnService.ACTION_CONNECT
            putExtra(WireGuardVpnService.EXTRA_SERVER, server)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun disconnect() {
        _state.value = VpnState.Disconnecting
        val intent = Intent(context, WireGuardVpnService::class.java).apply {
            action = WireGuardVpnService.ACTION_DISCONNECT
        }
        context.stopService(intent)
    }

    fun onVpnStateChanged(newState: VpnState) {
        _state.value = newState
    }

    companion object {
        const val REQUEST_VPN_PERMISSION = 0x100
    }
}
