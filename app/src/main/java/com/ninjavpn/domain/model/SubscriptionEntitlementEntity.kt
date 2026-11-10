package com.ninjavpn.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscription_entitlements")
data class SubscriptionEntitlementEntity(
    @PrimaryKey val id: String = "default",
    val tier: String,
    val expiryMillis: Long?,
    val isActive: Boolean,
    val features: String // JSON encoded set of features
)
