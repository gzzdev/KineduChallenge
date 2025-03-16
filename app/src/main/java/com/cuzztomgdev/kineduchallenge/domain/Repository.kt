package com.cuzztomgdev.kineduchallenge.domain

import com.cuzztomgdev.kineduchallenge.domain.model.Comic

interface Repository {
    suspend fun getComics(offset: Int = 0, limit: Int = 100): List<Comic>
}