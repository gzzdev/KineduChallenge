package com.cuzztomgdev.kineduchallenge.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cuzztomgdev.kineduchallenge.data.local.entity.CreatorEntity

@Dao
interface CreatorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(creator: CreatorEntity)

    @Query("SELECT * FROM creators WHERE id = :creatorId")
    suspend fun getCreatorById(creatorId: Int): CreatorEntity?

}