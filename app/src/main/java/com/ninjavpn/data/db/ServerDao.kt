package com.ninjavpn.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import com.ninjavpn.domain.model.Server

@Dao
interface ServerDao {
    @Query("SELECT * FROM servers ORDER BY loadPercent ASC, ping ASC")
    fun getAllServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE country = :country ORDER BY loadPercent ASC, ping ASC")
    fun getByCountry(country: String): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE id = :id")
    fun getById(id: String): Flow<ServerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(servers: List<ServerEntity>)

    @Transaction
    @Query("DELETE FROM servers")
    suspend fun clear()

    // REMOVED: getRandomFreeServer(), getPremiumServers(), getFreeServers()
}
