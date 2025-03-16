package com.cuzztomgdev.kineduchallenge.data.network.response

import com.cuzztomgdev.kineduchallenge.data.dto.ComicDTO

data class ComicWrapper (
    val code: Int,
    val status: String,
    val copyright: String,
    val data: MarvelData<ComicDTO>
)