package com.ninjavpn.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ninjavpn.domain.model.SubscriptionEntitlementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntitlementDao {
    @Query("SELECT * FROM subscription_entitlements WHERE id = :id")
    fun getById(id: String): Flow<SubscriptionEntitlementEntity?>

    @Query("SELECT * FROM subscription_entitlements WHERE id = 'default'")
    fun getDefault(): Flow<SubscriptionEntitlementEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entitlement: SubscriptionEntitlementEntity)

    @Query("DELETE FROM subscription_entitlements")
    suspend fun clear()
}
