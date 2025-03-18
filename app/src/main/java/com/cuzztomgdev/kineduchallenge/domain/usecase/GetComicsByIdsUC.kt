package com.cuzztomgdev.kineduchallenge.domain.usecase

import com.cuzztomgdev.kineduchallenge.domain.Repository
import javax.inject.Inject

class GetComicsByIdsUC @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(comicsIds: List<Int>) =
        repository.getComicsByIds(comicsIds)
}
