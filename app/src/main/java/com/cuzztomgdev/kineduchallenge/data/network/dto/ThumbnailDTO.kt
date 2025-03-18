package com.cuzztomgdev.kineduchallenge.data.network.dto

data class ThumbnailDTO(
    val path: String,
    val extension: String
) {
    fun getUri(): String {
        val pathHttp = path.replace("http", "https")
        return "$pathHttp.$extension"
    }
}