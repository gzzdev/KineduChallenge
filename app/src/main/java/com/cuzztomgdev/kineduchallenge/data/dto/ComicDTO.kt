package com.cuzztomgdev.kineduchallenge.data.dto

data class ComicDTO(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: ThumbnailDTO
)
