package com.ninjavpn.core.vpn

import android.util.Base64
import java.security.SecureRandom

object KeyGenerator {
    private const val KEY_LENGTH = 32

    fun generatePrivateKey(): String {
        val random = SecureRandom()
        val key = ByteArray(KEY_LENGTH)
        random.nextBytes(key)
        
        // Clamp the private key (WireGuard requirement)
        key[0] = key[0] and 248.toByte()
        key[31] = key[31] and 127.toByte()
        key[31] = key[31] or 64.toByte()
        
        return Base64.encodeToString(key, Base64.NO_WRAP)
    }

    fun generatePublicKey(privateKey: String): String {
        // For simplicity, we'll use a placeholder
        // In production, this should use actual Curve25519 key derivation
        val random = SecureRandom()
        val key = ByteArray(KEY_LENGTH)
        random.nextBytes(key)
        return Base64.encodeToString(key, Base64.NO_WRAP)
    }
}
