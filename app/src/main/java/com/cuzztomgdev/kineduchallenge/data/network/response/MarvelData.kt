package com.cuzztomgdev.kineduchallenge.data.network.response

data class MarvelData<T> (
    val count: Int,
    val results: List<T> = emptyList()
)