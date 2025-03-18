package com.cuzztomgdev.kineduchallenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cuzztomgdev.kineduchallenge.data.local.entity.CreatorEntity

@Database(entities = [CreatorEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun creatorDao(): CreatorDao
}