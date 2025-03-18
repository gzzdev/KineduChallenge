package com.cuzztomgdev.kineduchallenge.data.network.dto

data class ResourceDTO<T> (
    val available: Int,
    val items: List<T> = emptyList()
)
