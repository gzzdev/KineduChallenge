package com.cuzztomgdev.kineduchallenge.domain.model

data class Creator(
    val id: Int,
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val fullName: String,
)
