package com.cuzztomgdev.kineduchallenge.data.core.mapper

import com.cuzztomgdev.kineduchallenge.data.dto.ComicDTO
import com.cuzztomgdev.kineduchallenge.domain.model.Comic

fun ComicDTO.toDomain(): Comic =
    Comic(
        id = this.id,
        title = this.title,
        description=this.description,
        imageUri=this.thumbnail.getUri()
    )
