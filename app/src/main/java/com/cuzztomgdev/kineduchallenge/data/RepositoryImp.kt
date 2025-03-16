package com.cuzztomgdev.kineduchallenge.data

import android.util.Log
import com.cuzztomgdev.kineduchallenge.BuildConfig
import com.cuzztomgdev.kineduchallenge.data.mapper.toDomain
import com.cuzztomgdev.kineduchallenge.data.network.MarvelApiService
import com.cuzztomgdev.kineduchallenge.data.utils.generateHash
import com.cuzztomgdev.kineduchallenge.data.utils.getTimestamp
import com.cuzztomgdev.kineduchallenge.domain.Repository
import com.cuzztomgdev.kineduchallenge.domain.model.Comic

class RepositoryImp(private val marvelApiService: MarvelApiService) : Repository {

    override suspend fun getComics(offset: Int, limit: Int): List<Comic> {
        val credentials = getCredentials()
        runCatching {
                marvelApiService.getComics(
                    credentials.first,
                    credentials.second.first,
                    credentials.second.second,
                    offset,
                    limit
                )
            }.onSuccess { return it.data.result.map { dao -> dao .toDomain() } }
            .onFailure { Log.i("Repository", "GetComics error: ${it.message}") }
        return emptyList()
    }

    private fun getCredentials(): Pair<String, Pair<String, String>> {
        val ts = getTimestamp()
        val hash = generateHash(ts, BuildConfig.PRIVATE_API_KEY, BuildConfig.PUBLIC_API_KEY)
        return Pair(ts, Pair(BuildConfig.PUBLIC_API_KEY, hash))
    }
}