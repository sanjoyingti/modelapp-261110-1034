package com.ninjavpn.domain.model

sealed interface VpnState {
    data class Disconnected(val reason: String?) : VpnState
    data class Connecting(val server: Server) : VpnState
    data class Connected(val server: Server, val stats: TrafficStats) : VpnState
    data class Disconnecting : VpnState
    data class Error(val message: String, val retryable: Boolean) : VpnState
}

data class TrafficStats(
    val rxBytes: Long = 0L,
    val txBytes: Long = 0L,
    val rxSpeed: Long = 0L,
    val txSpeed: Long = 0L,
    val durationSeconds: Long = 0L,
)