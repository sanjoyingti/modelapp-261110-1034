package com.ninjavpn.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.model.TrafficLog
import com.ninjavpn.domain.model.SubscriptionEntitlementEntity

@Database(
    entities = [ServerEntity::class, TrafficLog::class, SubscriptionEntitlementEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
    abstract fun trafficDao(): TrafficDao
    abstract fun entitlementDao(): EntitlementDao
}