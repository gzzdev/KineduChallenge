package com.cuzztomgdev.kineduchallenge.domain

import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.domain.model.Creator

interface Repository {
    suspend fun getComics(offset: Int, limit: Int): List<Comic>
    suspend fun getComicsByIds(comicsId: List<Int>): List<Comic>

    suspend fun getCreatorById(creatorId: Int): Creator?
    //suspend fun getCreatorData(creatorId: Int): Crea
}