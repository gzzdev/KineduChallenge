package com.cuzztomgdev.kineduchallenge.data.network.response

import com.cuzztomgdev.kineduchallenge.data.network.dto.ComicDTO

data class ComicsResponse (
    val code: Int,
    val status: String,
    val copyright: String,
    val data: MarvelData<ComicDTO>
)