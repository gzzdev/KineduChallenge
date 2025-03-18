package com.cuzztomgdev.kineduchallenge.domain.model

data class Creator(
    val id: Int,
    val name: String,
    val role: String,
    val imageUri: String,
    val comics: List<Int>
)
