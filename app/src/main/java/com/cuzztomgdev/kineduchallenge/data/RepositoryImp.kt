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

    override suspend fun getCreatorById(creatorId: Int): Creator? {
        var creator = creatorDao.getCreatorById(creatorId)?.toDomain()
        if (creator == null)
            return creator

        if (creator.imageUri.isNotEmpty())
            return creator

        val imgUri = getCreatorImage(creatorId)
        creator = creator.copy(imageUri = imgUri)
        creatorDao.update(creator.toEntity())
        return creator
    }

    override suspend fun getCreatorImage(creatorId: Int): String {
        runCatching {
            marvelApiService.getCreator(creatorId)
        }.onSuccess { response ->
            Log.i("Main Repository", "GetComics: $response")
            return response.data.results.first().toDomain().imageUri
        }.onFailure {
            Log.i("Main Repository", "GetComics error: ${it.message}")
        }
        return ""
    }
}