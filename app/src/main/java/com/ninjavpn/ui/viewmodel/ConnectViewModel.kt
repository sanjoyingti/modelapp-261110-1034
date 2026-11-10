package com.ninjavpn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninjavpn.core.vpn.VpnManager
import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.model.VpnState
import com.ninjavpn.domain.repository.ServerRepository
import com.ninjavpn.domain.usecase.GetBestServerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val vpnManager: VpnManager,
    private val serverRepo: ServerRepository,
    private val getBestServer: GetBestServerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val vpnState: StateFlow<VpnState> = vpnManager.state

    init {
        observeVpnState()
    }

    private fun observeVpnState() {
        viewModelScope.launch {
            vpnManager.state.collect { state ->
                _uiState.update { it.copy(vpnState = state) }
            }
        }
    }

    fun connectBest() {
        viewModelScope.launch {
            val server = getBestServer()
            server?.let { vpnManager.connect(it) }
        }
    }

    fun connect(server: Server) {
        vpnManager.connect(server)
    }

    fun disconnect() {
        vpnManager.disconnect()
    }

    fun retry() {
        _uiState.value.vpnState?.let {
            if (it is VpnState.Error && it.retryable) {
                connectBest()
            }
        }
    }

    data class UiState(
        val vpnState: VpnState? = null,
    )
}