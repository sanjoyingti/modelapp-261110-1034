package com.ninjavpn.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "traffic_logs")
data class TrafficLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serverId: String,
    val timestamp: Long,
    val rxBytes: Long,
    val txBytes: Long,
    val durationSeconds: Long
)
