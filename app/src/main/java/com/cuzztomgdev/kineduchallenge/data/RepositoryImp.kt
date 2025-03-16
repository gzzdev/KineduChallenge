package com.cuzztomgdev.kineduchallenge.data

import android.util.Log
import com.cuzztomgdev.kineduchallenge.BuildConfig
import com.cuzztomgdev.kineduchallenge.data.core.mapper.toDomain
import com.cuzztomgdev.kineduchallenge.data.network.MarvelApiService
import com.cuzztomgdev.kineduchallenge.data.core.utils.generateHash
import com.cuzztomgdev.kineduchallenge.data.core.utils.getTimestamp
import com.cuzztomgdev.kineduchallenge.domain.Repository
import com.cuzztomgdev.kineduchallenge.domain.model.Comic

class RepositoryImp(private val marvelApiService: MarvelApiService) : Repository {

    override suspend fun getComics(offset: Int, limit: Int): List<Comic> {
        runCatching {
                marvelApiService.getComics(
                    offset,
                    limit
                )
            }.onSuccess { return it.data.result.map { dao -> dao .toDomain() } }
            .onFailure { Log.i("Repository", "GetComics error: ${it.message}") }
        return emptyList()
    }
}