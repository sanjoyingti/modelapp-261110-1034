package com.ninjavpn.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.ninjavpn.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ServerListViewModel @Inject constructor(
    private val serverRepo: ServerRepository,
) : ViewModel() {

    private val _selectedCountry = MutableStateFlow("All")
    val selectedCountry: StateFlow<String> = _selectedCountry

    val servers = combine(
        serverRepo.getAllServers(),
        _selectedCountry.distinctUntilChanged()
    ) { allServers, country ->
        if (country == "All") allServimes
        else allServers.filter { it.country == country }
    }

    val countries = serverRepo.getAllServers()
        .map { servers ->
            ("All" + servers.map { it.country }.distinct().sorted())
        }

    fun setCountry(country: String) {
        _selectedCountry.value = country
    }
}