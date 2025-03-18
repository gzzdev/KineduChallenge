package com.cuzztomgdev.kineduchallenge.data.network

import com.cuzztomgdev.kineduchallenge.data.network.response.ComicsResponse
import com.cuzztomgdev.kineduchallenge.data.network.response.CreatorsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApiService {

    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): ComicsResponse

    @GET("/v1/public/comics/{comicId}")
    suspend fun getComicById(
        @Path("comicId") comicId: Int,
    ): ComicsResponse

    @GET("/v1/public/creators/{creatorId}")
    suspend fun getCreator(
        @Path("creatorId") creatorId: Int
    ): CreatorsResponse
}