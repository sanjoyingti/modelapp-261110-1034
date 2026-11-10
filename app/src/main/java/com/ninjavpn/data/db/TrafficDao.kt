package com.ninjavpn.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ninjavpn.domain.model.TrafficLog
import kotlinx.coroutines.flow.Flow

@Dao
interface TrafficDao {
    @Query("SELECT * FROM traffic_logs WHERE serverId = :serverId ORDER BY timestamp DESC")
    fun getByServer(serverId: String): Flow<List<TrafficLog>>

    @Query("SELECT * FROM traffic_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAll(): Flow<List<TrafficLog>>

    @Insert
    suspend fun insert(log: TrafficLog)

    @Query("DELETE FROM traffic_logs WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOlderThan(beforeTimestamp: Long)

    @Query("DELETE FROM traffic_logs")
    suspend fun clear()
}
