package com.ninjavpn.ui.screen.servers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ninjavpn.domain.model.ObfuscationType
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            countries.forEach { country ->
                FilterChip(
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = NinjaVpnTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Flag + Name + Protocol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = flagEmoji(server.country),
                        fontSize = 24.sp
                    )
                    Column {
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
                Box(
                    modifier = Modifier.size(48.dp, 6.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val load = server.loadPercent / 100f
                        drawRect(
                            color = NinjaVpnTheme.colorScheme.surfaceContainerHighest,
                            size = size
                        )
                        drawRect(
                            color = when {
                                load < 0.5f -> NinjaVpnTheme.colorScheme.primary
                                load < 0.8f -> NinjaVpnTheme.colorScheme.secondary
                                else -> NinjaVpnTheme.colorScheme.error
                            },
                            size = Size(size.width * load, size.height)
                        )
                    }
                }
            }

            // Ping row
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                server.ping?.let { ping ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SignalWifi4Bar,
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
                if (server.obfuscation != ObfuscationType.NONE) {
                    Badge(
                        containerColor = NinjaVpnTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = server.obfuscation.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = NinjaVpnTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

private fun flagEmoji(countryCode: String): String {
    return countryCode.uppercase().toCharArray()
        .map { Character.toChars(0x1F1E5 + it.code - 'A'.code) }
        .joinToString("")
}
