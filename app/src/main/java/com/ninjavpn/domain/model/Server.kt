package com.ninjavpn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Server(
    val id: String,
    val name: String,
    val hostname: String,
    val port: Int,
    val protocol: Protocol,
    val country: String,
    val city: String?,
    val ping: Int?,
    val loadPercent: Int,
    val obfuscation: ObfuscationType,
    val publicKey: String,
    val allowedIPs: List<String> = listOf("0.0.0.0/0", "::/0"),
    val dns: List<String> = listOf("1.1.1.1", "8.8.8.8"),
    val mtU: Int = 1280,
    val keepAlive: Int = 25,
) : Parcelable

enum class Protocol { WIREGUARD, VLESS, VMSS, SHADOWSOCKS, TROJAN }
enum class ObfuscationType { NONE, TLS, REALITY, WS, GRPC, H2 }
