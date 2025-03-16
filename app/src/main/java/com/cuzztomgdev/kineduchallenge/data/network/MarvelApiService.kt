package com.cuzztomgdev.kineduchallenge.data.network

import com.cuzztomgdev.kineduchallenge.data.network.response.ComicWrapper
import retrofit2.http.GET

interface MarvelApiService {

    @GET("/v1/public/comics")
    suspend fun getComics(): ComicWrapper
}