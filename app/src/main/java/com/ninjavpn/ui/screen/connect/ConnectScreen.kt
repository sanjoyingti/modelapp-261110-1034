package com.ninjavpn.ui.screen.connect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ninjavpn.domain.model.VpnState
import com.ninjavpn.ui.theme.NinjaVpnTheme
import com.ninjavpn.ui.viewmodel.ConnectViewModel
import kotlinx.coroutines.launch

@Composable
fun ConnectScreen(
    viewModel: ConnectViewModel = viewModel(),
    onServerClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val vpnState by viewModel.vpnState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Connection Button
        Card(
            modifier = Modifier.size(200.dp),
            elevation = if (vpnState is VpnState.Connected) 8.dp else 4.dp,
            colors = CardDefaults.cardColors(
                containerColor = when (vpnState) {
                    is VpnState.Connected -> NinjaVpnTheme.colorScheme.primary
                    is VpnState.Connecting -> NinjaVpnTheme.colorScheme.secondaryContainer
                    is VpnState.Error -> NinjaVpnTheme.colorScheme.errorContainer
                    else -> NinjaVpnTheme.colorScheme.surfaceContainerHighest
                }
            )
        ) {
            Button(
                onClick = {
                    when (vpnState) {
                        is VpnState.Connected -> viewModel.disconnect()
                        is VpnState.Disconnected -> viewModel.connectBest()
                        is VpnState.Error -> viewModel.retry()
                        else -> Unit
                    }
                },
                modifier = Modifier.fillMaxSize(),
                enabled = vpnState !in VpnState.Connecting..VpnState.Disconnecting,
            ) {
                Text(
                    text = when (vpnState) {
                        is VpnState.Connected -> "DISCONNECT"
                        is VpnState.Connecting -> "CONNECTING..."
                        is VpnState.Disconnecting -> "DISCONNECTING..."
                        is VpnState.Error -> "RETRY"
                        else -> "CONNECT"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (vpnState) {
                        is VpnState.Connected -> Color.White
                        else -> NinjaVpnTheme.colorScheme.onSurface
                    }
                )
            }
        }

        // Status Text
        Text(
            text = when (vpnState) {
                is VpnState.Connected -> "Connected to ${(vpnState as VpnState.Connected).server.name}"
                is VpnState.Connecting -> "Connecting to ${vpnState.server.name}..."
                is VpnState.Error -> "Error: ${vpnState.message}"
                else -> "Tap to connect"
            },
            fontSize = 16.sp,
            color = NinjaVpnTheme.colorScheme.onSurfaceVariant
        )

        // Stats
        if (vpnState is VpnState.Connected) {
            val stats = vpnState.stats
            Text(
                text = "↑ ${formatSpeed(stats.txSpeed)}  ↓ ${formatSpeed(stats.rxSpeed)}  •  ${formatDuration(stats.durationSeconds)}",
                fontSize = 14.sp,
                color = NinjaVpnTheme.colorScheme.onSurfaceVariant
            )
        }

        // Change Server Button
        androidx.compose.material3.TextButton(
            onClick = onServerClick,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("CHANGE SERVER", fontWeight = FontWeight.Medium)
        }
    }
}

private fun formatSpeed(bytes: Long): String = when {
    bytes >= 1_000_000 -> "${(bytes / 1_000_000).toString()} Mbps"
    bytes >= 1_000 -> "${(bytes / 1_000).toString()} Kbps"
    else -> "${bytes} bps"
}

private fun formatDuration(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) "%02d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}