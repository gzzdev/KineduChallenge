package com.cuzztomgdev.kineduchallenge.data.network.dto

data class ComicCreatorDTO(
    val name: String,
    val role: String,
    val resourceURI: String // "http://gateway.marvel.com/v1/public/creators/1234"
) {
    fun getIdFromResourceURI(): Int {
        return resourceURI.split("/").lastOrNull()?.toIntOrNull() ?: -1
    }
}