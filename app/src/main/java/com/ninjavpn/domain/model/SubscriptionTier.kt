package com.ninjavpn.domain.model

enum class SubscriptionTier {
    FREE,
    PREMIUM_MONTHLY,
    PREMIUM_YEARLY,
    PREMIUM_LIFETIME
}

data class Entitlement(
    val tier: SubscriptionTier,
    val expiryMillis: Long?,
    val isActive: Boolean,
    val features: Set<PremiumFeature>
)

enum class PremiumFeature {
    UNLIMITED_BANDWIDTH,
    ALL_SERVERS,
    OBFS_PROTOCOLS,      // VLESS, Reality, etc.
    CUSTOM_DNS,
    KILL_SWITCH,
    SPLIT_TUNNELING,
    PORT_FORWARDING,
    MULTI_HOP,
    AD_BLOCKER,
    SPEED_TEST,
}