package com.cuzztomgdev.kineduchallenge.ui.home

import com.cuzztomgdev.kineduchallenge.domain.model.Comic

sealed class ComicsState {
    data object Start: ComicsState()
    data object Loading: ComicsState()
    data class Error(val error: String): ComicsState()
    data class Success(val comics:List<Comic>): ComicsState()
}