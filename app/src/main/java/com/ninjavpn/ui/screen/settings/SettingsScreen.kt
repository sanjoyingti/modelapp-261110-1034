package com.ninjavpn.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ninjavpn.ui.theme.NinjaVpnTheme
import com.ninjavpn.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val killSwitch by viewModel.killSwitch.collectAsStateWithLifecycle()
    val autoConnect by viewModel.autoConnect.collectAsStateWithLifecycle()
    val protocol by viewModel.protocol.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("SETTINGS", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NinjaVpnTheme.colorScheme.primary)
        Divider(modifier = Modifier.padding(vertical = 16.dp))

        SettingsRow(
            title = "Kill Switch",
            subtitle = "Block all traffic if VPN disconnects",
            trailing = { Switch(checked = killSwitch, onCheckedChange = viewModel::setKillSwitch) }
        )

        SettingsRow(
            title = "Auto-Connect",
            subtitle = "Connect automatically on app launch",
            trailing = { Switch(checked = autoConnect, onCheckedChange = viewModel::setAutoConnect) }
        )

        SettingsRow(
            title = "Protocol",
            subtitle = protocol,
            trailing = { Text(protocol, color = NinjaVpnTheme.colorScheme.primary) },
            onClick = { viewModel.showProtocolDialog() }
        )

        SettingsRow(
            title = "Custom DNS",
            subtitle = "Override DNS servers (optional)",
            trailing = { 
                androidx.compose.material.icons.filled.ArrowForwardIos(
                    tint = NinjaVpnTheme.colorScheme.onSurfaceVariant
                )
            },
            onClick = { viewModel.showDnsDialog() }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        SettingsRow(
            title = "Speed Test",
            subtitle = "Measure VPN performance",
            trailing = { 
                androidx.compose.material.icons.filled.ArrowForwardIos(
                    tint = NinjaVpnTheme.colorScheme.onSurfaceVariant
                )
            },
            onClick = { /* navigate */ }
        )

        SettingsRow(
            title = "Clear Server Cache",
            subtitle = "Force refresh server list from built-in config",
            trailing = { 
                androidx.compose.material.icons.filled.Refresh(
                    tint = NinjaVpnTheme.colorScheme.onSurfaceVariant
                )
            },
            onClick = { viewModel.refreshServers() }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        SettingsRow(
            title = "About",
            subtitle = "Version 1.4.13-free • WireGuard® • Open Source",
            trailing = { 
                androidx.compose.material.icons.filled.Info(
                    tint = NinjaVpnTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = { Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(subtitle, fontSize = 13.sp, color = NinjaVpnTheme.colorScheme.onSurfaceVariant) },
        trailingContent = trailing,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .then(onClick?.let { androidx.compose.foundation.gestures.clickable { it() } } ?: Modifier),
    )
}