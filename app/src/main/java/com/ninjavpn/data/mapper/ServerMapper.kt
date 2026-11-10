package com.ninjavpn.data.mapper

import com.ninjavpn.data.db.ServerEntity
import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.model.Protocol
import com.ninjavpn.domain.model.ObfuscationType
import kotlinx.serialization.json.Json

object ServerMapper {
    private val json = Json { ignoreUnknownKeys = true }

    fun toDomain(entity: ServerEntity): Server = Server(
        id = entity.id,
        name = entity.name,
        hostname = entity.hostname,
        port = entity.port,
        protocol = entity.protocol,
        country = entity.country,
        city = entity.city,
        ping = entity.ping,
        loadPercent = entity.loadPercent,
        // REMOVED: isPremium = entity.isPremium,
        obfuscation = entity.obfuscation,
        publicKey = entity.publicKey,
        allowedIPs = json.decodeFromString<List<String>>(entity.allowedIPs),
        dns = json.decodeFromString<List<String>>(entity.dns),
        mtU = entity.mtU,
        keepAlive = entity.keepAlive,
    )

    fun toEntity(server: Server): ServerEntity = ServerEntity(
        id = server.id,
        name = server.name,
        hostname = server.hostname,
        port = server.port,
        protocol = server.protocol,
        country = server.country,
        city = server.city,
        ping = server.ping,
        loadPercent = server.loadPercent,
        // REMOVED: isPremium = server.isPremium,
        obfuscation = server.obfuscation,
        publicKey = server.publicKey,
        allowedIPs = json.encodeToString(server.allowedIPs),
        dns = json.encodeToString(server.dns),
        mtU = server.mtU,
        keepAlive = server.keepAlive,
    )
}