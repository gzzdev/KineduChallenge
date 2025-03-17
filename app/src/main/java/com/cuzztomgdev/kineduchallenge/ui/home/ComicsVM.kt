package com.cuzztomgdev.kineduchallenge.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuzztomgdev.kineduchallenge.domain.usecase.GetComicsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ComicsVM @Inject constructor(private val getComicsUC: GetComicsUC): ViewModel() {

    private var _state = MutableStateFlow<ComicsState>(ComicsState.Loading)
    val comicsState: StateFlow<ComicsState> = _state

    fun getComics(offset: Int = 0, limit: Int = 12) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ComicsState.Loading
            // Secondary Thread
            val comics = getComicsUC(offset, limit)
            if (comics.isEmpty()) {
                _state.value = ComicsState.Error("No se encontraron comics")
            } else {
                _state.value = ComicsState.Success(comics)
            }
        }
    }

    fun isLoading(): Boolean = comicsState.value is ComicsState.Loading

}