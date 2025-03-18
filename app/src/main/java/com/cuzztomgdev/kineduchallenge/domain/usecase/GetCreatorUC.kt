package com.cuzztomgdev.kineduchallenge.domain.usecase

import com.cuzztomgdev.kineduchallenge.domain.Repository
import com.cuzztomgdev.kineduchallenge.domain.model.Creator
import javax.inject.Inject

class GetCreatorUC @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(creatorId: Int): Creator? =
        repository.getCreatorById(creatorId)
}
