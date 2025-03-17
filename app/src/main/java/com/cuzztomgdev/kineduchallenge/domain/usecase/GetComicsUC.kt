package com.cuzztomgdev.kineduchallenge.domain.usecase

import com.cuzztomgdev.kineduchallenge.domain.Repository
import javax.inject.Inject

class GetComicsUC @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(offset: Int = 0, limit: Int = 100) = repository.getComics()
}
