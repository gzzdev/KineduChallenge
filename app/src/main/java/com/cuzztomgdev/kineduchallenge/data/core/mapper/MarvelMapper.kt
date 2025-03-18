package com.cuzztomgdev.kineduchallenge.data.core.mapper

import com.cuzztomgdev.kineduchallenge.data.local.entity.CreatorEntity
import com.cuzztomgdev.kineduchallenge.data.network.dto.ComicDTO
import com.cuzztomgdev.kineduchallenge.data.network.dto.CreatorDTO
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.domain.model.Creator

fun ComicDTO.toDomain(): Comic =
    Comic(
        id = this.id,
        title = this.title,
        description=this.description.orEmpty(),
        imageUri=this.thumbnail.getUri(),
        creators = this.creators.items.map { it -> it.getIdFromResourceURI() }
    )

fun CreatorDTO.toDomain(): Creator = Creator(
    id = this.id,
    name = this.fullName,
    role = "",
    imageUri = this.thumbnail.getUri(),
    comics = this.comics.items.map { it -> it.getIdFromResourceURI() }
)

fun CreatorEntity.toDomain(): Creator = Creator(
    this.id, this.name, this.role,
    this.resourceURI, emptyList()
)

fun Creator.toEntity(): CreatorEntity = CreatorEntity(this.id, this.name, this.role, this.imageUri)