package com.cuzztomgdev.kineduchallenge.data.network.response

import com.cuzztomgdev.kineduchallenge.data.network.dto.CreatorDTO

data class CreatorsResponse (
    val code: Int,
    val status: String,
    val copyright: String,
    val data: MarvelData<CreatorDTO>
)