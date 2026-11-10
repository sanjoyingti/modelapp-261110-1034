package com.ninjavpn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninjavpn.data.prefs.PreferencesRepository
import com.ninjavpn.domain.model.Protocol
import com.ninjavpn.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val serverRepo: ServerRepository,
) : ViewModel() {

    private val _killSwitch = MutableStateFlow(false)
    val killSwitch: StateFlow<Boolean> = _killSwitch

    private val _autoConnect = MutableStateFlow(true)
    val autoConnect: StateFlow<Boolean> = _autoConnect

    private val _protocol = MutableStateFlow("WIREGUARD")
    val protocol: StateFlow<String> = _protocol

    init {
        loadPrefs()
    }

    private fun loadPrefs() {
        viewModelScope.launch {
            _killSwitch.value = prefs.getKillSwitch()
            _autoConnect.value = prefs.getAutoConnect()
            _protocol.value = prefs.getProtocol()
        }
    }

    fun setKillSwitch(enabled: Boolean) {
        _killSwitch.value = enabled
        viewModelScope.launch { prefs.setKillSwitch(enabled) }
    }

    fun setAutoConnect(enabled: Boolean) {
        _autoConnect.value = enabled
        viewModelScope.launch { prefs.setAutoConnect(enabled) }
    }

    fun setProtocol(protocol: String) {
        _protocol.value = protocol
        viewModelScope.launch { prefs.setProtocol(protocol) }
    }

    fun showProtocolDialog() { /* show bottom sheet with Protocol.values() */ }

    fun showDnsDialog() { /* show DNS input dialog */ }

    fun refreshServers() {
        viewModelScope.launch { serverRepo.refreshFromAssets() }
    }
}