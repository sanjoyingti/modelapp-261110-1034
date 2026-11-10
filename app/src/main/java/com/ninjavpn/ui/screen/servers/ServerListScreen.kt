package com.ninjavpn.ui.screen.servers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import com.ninjavpn.ui.theme.NinjaVpnTheme
import com.ninjavpn.ui.viewmodel.ServerListViewModel

@Composable
fun ServerListScreen(
    viewModel: ServerListViewModel = viewModel(),
    onServerSelected: (String) -> Unit,
) {
    val servers by viewModel.servers.collectAsStateWithLifecycle(emptyList())
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle("All")
    val countries by viewModel.countries.collectAsStateWithLifecycle(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        // Country Filter Chips
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            countries.forEach { country ->
                androidx.compose.material3.FilterChip(
                    selected = selectedCountry == country,
                    onClick = { viewModel.setCountry(country) },
                    label = { Text(country, fontSize = 12.sp) },
                    colors = androidx.compose.material3.ChipDefaults.filterChipColors(
                        selectedContainerColor = NinjaVpnTheme.colorScheme.primaryContainer,
                        containerColor = NinjaVpnTheme.colorScheme.surfaceContainerHighest
                    )
                )
            }
        }

        Divider()

        // Server List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            items(servers) { server ->
                ServerCard(
                    server = server,
                    onClick = { onServerSelected(server.id) }
                )
            }
        }
    }
}

@Composable
private fun ServerCard(
    server: com.ninjavpn.domain.model.Server,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = NinjaVpnTheme.colorScheme.surfaceContainerLow
        )
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Flag + Name + Protocol
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = flagEmoji(server.country),
                        fontSize = 24.sp
                    )
                    androidx.compose.foundation.layout.Column {
                        Text(
                            text = server.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NinjaVpnTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${server.city ?: server.country} • ${server.protocol.name}",
                            fontSize = 12.sp,
                            color = NinjaVpnTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Load indicator
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.size(48.dp, 6.dp)
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        val load = server.loadPercent / 100f
                        drawRect(
                            color = NinjaVpnTheme.colorScheme.surfaceContainerHighest,
                            size = size
                        )
                        drawRect(
                            color = when {
                                load < 0.5 -> NinjaVpnTheme.colorScheme.primary
                                load < 0.8 -> NinjaVpnTheme.colorScheme.secondary
                                else -> NinjaVpnTheme.colorScheme.error
                            },
                            size = androidx.compose.ui.geometry.Size(size.width * load, size.height)
                        )
                    }
                    Text(
                        text = "${server.loadPercent}%",
                        fontSize = 10.sp,
                        color = NinjaVpnTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Ping row
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                server.ping?.let { ping ->
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
                    ) {
                        androidx.compose.material.icons.filled.SignalWifi4Bar(
                            tint = when {
                                ping < 100 -> Color.Green
                                ping < 200 -> Color.Orange
                                else -> Color.Red
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${ping} ms",
                            fontSize = 12.sp,
                            color = when {
                                ping < 100 -> Color.Green
                                ping < 200 -> Color.Orange
                                else -> Color.Red
                            }
                        )
                    }
                }

                // Obfuscation badge
                if (server.obfuscation != com.ninjavpn.domain.model.ObfuscationType.NONE) {
                    androidx.compose.material3.Badge(
                        badgeContent = {
                            Text(
                                text = server.obfuscation.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = NinjaVpnTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        badgeColors = androidx.compose.material3.BadgeDefaults.badgeColors(
                            containerColor = NinjaVpnTheme.colorScheme.primaryContainer
                        )
                    ) {
                        androidx.compose.foundation.layout.Box()
                    }
                }

                // REMOVED: Premium lock badge
            }
        }
    }
}

private fun flagEmoji(countryCode: String): String {
    return countryCode.uppercase().toCharArray()
        .map { Character.toChars(0x1F1E5 + it - 'A') }
        .joinToString("")
}
