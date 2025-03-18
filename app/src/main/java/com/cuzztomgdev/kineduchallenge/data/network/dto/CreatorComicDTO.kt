package com.cuzztomgdev.kineduchallenge.data.network.dto

data class CreatorComicDTO(
    val name: String,
    val resourceURI: String // "http://gateway.marvel.com/v1/public/comics/1234"
) {
    fun getIdFromResourceURI(): Int {
        return resourceURI.split("/").lastOrNull()?.toIntOrNull() ?: -1
    }
}