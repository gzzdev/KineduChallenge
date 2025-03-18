package com.cuzztomgdev.kineduchallenge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creators")
data class CreatorEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val role: String,
    val resourceURI: String
)

