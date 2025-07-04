package com.cuzztomgdev.kineduchallenge.data.network.dto

data class ComicDTO(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: ThumbnailDTO,
    val creators: ResourceDTO<ComicCreatorDTO>
)


