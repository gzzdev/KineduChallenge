package com.cuzztomgdev.kineduchallenge.data.dto

data class ThumbnailDTO(
    val path: String,
    val extension: String
) {
    fun getUri(): String {
        return "$path.$extension"
    }
}