package com.ninjavpn.data.repository

import android.content.Context
import com.ninjavpn.data.db.AppDatabase
import com.ninjavpn.data.db.ServerDao
import com.ninjavpn.data.mapper.ServerMapper
import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.repository.ServerRepository as DomainServerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepository @Inject constructor(
    private val dao: ServerDao,
    private val context: Context,
) : DomainServerRepo {

    override fun getAllServers(): Flow<List<Server>> = dao.getAllServers()
        .map { it.map(ServerMapper::toDomain) }

    override fun getByCountry(country: String): Flow<List<Server>> = dao.getByCountry(country)
        .map { it.map(ServerMapper::toDomain) }

    override suspend fun refreshFromAssets() = withContext(Dispatchers.IO) {
        val json = context.assets.open("edge.json").bufferedReader().use { it.readText() }
        val servers = parseEdgeJson(json)
        dao.clear()
        dao.upsertAll(servers.map(ServerMapper::toEntity))
    }

    private fun parseEdgeJson(json: String): List<Server> {
        return kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            .decodeFromString<List<Server>>(json)
    }
}