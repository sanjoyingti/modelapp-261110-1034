package com.ninjavpn.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ninjavpn.domain.model.Protocol
import com.ninjavpn.domain.model.ObfuscationType
import kotlinx.serialization.Serializable

@Entity(tableName = "servers")
@Serializable
data class ServerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val hostname: String,
    val port: Int,
    val protocol: Protocol,
    val country: String,
    val city: String?,
    val ping: Int?,
    val loadPercent: Int,
    // REMOVED: val isPremium: Boolean,
    val obfuscation: ObfuscationType,
    val publicKey: String,
    val allowedIPs: String, // JSON list
    val dns: String,        // JSON list
    val mtU: Int,
    val keepAlive: Int,
)