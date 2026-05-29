package com.ikhwan.roomdatabse.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface KomputerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(komputer: Komputer)

    @Update
    suspend fun update(komputer: Komputer)

    @Delete
    suspend fun delete(komputer: Komputer)

    @Query("SELECT * FROM komputers ORDER BY id DESC")
    fun getAllKomputer(): Flow<List<Komputer>>
}
