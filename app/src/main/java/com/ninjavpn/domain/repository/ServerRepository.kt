package com.ninjavpn.domain.repository

import com.ninjavpn.domain.model.Server
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getAllServers(): Flow<List<Server>>
    fun getByCountry(country: String): Flow<List<Server>>
    suspend fun refreshFromAssets()
}