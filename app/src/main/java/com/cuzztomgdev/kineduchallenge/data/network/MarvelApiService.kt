package com.cuzztomgdev.kineduchallenge.data.network

import com.cuzztomgdev.kineduchallenge.data.network.response.ComicsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApiService {

    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("ts") ts: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("orderBy") orderBy: String = "modified"
    ): ComicsResponse
}