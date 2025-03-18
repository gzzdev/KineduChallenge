package com.cuzztomgdev.kineduchallenge.ui.common.state

import com.cuzztomgdev.kineduchallenge.domain.model.Creator

sealed class CreatorState {
    data object Start: CreatorState()
    data object Loading: CreatorState()
    data class Error(val error: String): CreatorState()
    data class Success(val creator: Creator): CreatorState()
}