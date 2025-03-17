package com.cuzztomgdev.kineduchallenge.domain.model

data class Comic(
    val id: Int,
    val title: String,
    val imageUri: String,
    val description: String? = ""
)
