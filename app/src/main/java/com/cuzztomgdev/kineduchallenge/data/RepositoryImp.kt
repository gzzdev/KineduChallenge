package com.cuzztomgdev.kineduchallenge.data

import android.util.Log
import com.cuzztomgdev.kineduchallenge.data.core.mapper.toDomain
import com.cuzztomgdev.kineduchallenge.data.core.mapper.toEntity
import com.cuzztomgdev.kineduchallenge.data.local.CreatorDao
import com.cuzztomgdev.kineduchallenge.data.local.entity.CreatorEntity
import com.cuzztomgdev.kineduchallenge.data.network.MarvelApiService
import com.cuzztomgdev.kineduchallenge.domain.Repository
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.domain.model.Creator
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val marvelApiService: MarvelApiService,
    private val creatorDao: CreatorDao
) : Repository {

    override suspend fun getComics(offset: Int, limit: Int): List<Comic> {
        runCatching {
                marvelApiService.getComics(offset, limit)
            }.onSuccess { response ->
                response.data.results.forEach {
                    try {
                        it.creators.items.forEach { creator ->
                            Log.i("Main Repository", "GetComics: $creator")
                            creatorDao.insert(
                                CreatorEntity(
                                    creator.getIdFromResourceURI(),
                                    creator.name,
                                    creator.role,
                                    ""
                                )
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
                return response.data.results.map { dao ->
                    dao.toDomain()
                }
            }.onFailure {
                Log.i("Main Repository", "GetComics error: ${it.message}")
            }
        return emptyList()
    }

    override suspend fun getComicsByIds(comicsIds: List<Int>): List<Comic> {
        return coroutineScope {
            comicsIds.map { comicId ->
                async {
                    marvelApiService
                        .getComicById(comicId)
                        .data.results
                        .firstOrNull()?.toDomain()
                }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun getCreatorById(creatorId: Int): Creator? {
        var _creator = creatorDao.getCreatorById(creatorId)?.toDomain()
        runCatching {
            marvelApiService.getCreator(creatorId)
        }.onSuccess { response ->
            var creator = response.data.results.first().toDomain()
            creator = creator.copy(role = _creator?.role.orEmpty(), comics = creator.comics)
            if (_creator!=null)
                creatorDao.update(creator.toEntity())
            else
                creatorDao.insert(creator.toEntity())
            return creator
        }.onFailure {
            Log.i("Main Repository", "GetComics error: ${it.message}")
        }
        return null
    }

    /*override suspend fun getCreatorData(creatorId: Int): Creator? {
        runCatching {
            marvelApiService.getCreator(creatorId)
        }.onSuccess { response ->
            Log.i("Main Repository", "GetComics: $response")
            return response.data.results.first().toDomain()
        }.onFailure {
            Log.i("Main Repository", "GetComics error: ${it.message}")
        }
        return null
    }*/
}