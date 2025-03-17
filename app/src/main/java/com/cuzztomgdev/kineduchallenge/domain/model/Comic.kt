package com.cuzztomgdev.kineduchallenge.domain.model

import java.io.Serializable

data class Comic(
    val id: Int,
    val title: String,
    val imageUri: String,
    val description: String? = ""
): Serializable
