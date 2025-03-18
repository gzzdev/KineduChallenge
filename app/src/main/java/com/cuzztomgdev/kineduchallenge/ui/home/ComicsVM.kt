package com.cuzztomgdev.kineduchallenge.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.domain.usecase.GetComicsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicsVM @Inject constructor(private val getComicsUC: GetComicsUC): ViewModel() {
    private val _comics = mutableListOf<Comic>()
    private var _state = MutableStateFlow<ComicsState>(ComicsState.Start)
    val comicsState: StateFlow<ComicsState> = _state

    fun getComics(scroll: Boolean = false, offset: Int = 0, limit: Int = 12) {
        if (!scroll && isLoading()) { // Block to prevent multiple requests
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ComicsState.Loading
            // Secondary Thread
            val newComics = getComicsUC(offset, limit)
            if (newComics.isEmpty()) {
                _state.value = ComicsState.Error("No se encontraron comics")
            } else {
                val uniqueComics = newComics.filter { newComic ->
                    _comics.none { existingComic -> existingComic.id == newComic.id }
                }
                _comics.addAll(uniqueComics)
                _state.value = ComicsState.Success(_comics.toList())
            }
        }
    }

    fun searchComics(query: String) {
        if (isLoading()) {
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            _state.value = ComicsState.Loading
            Log.i("Main Activity", "searchComics: $query")
            if (query.isEmpty()) {
                _state.value = ComicsState.Success(_comics.toList())
            } else {
                val filteredComics = _comics.filter { comic ->
                    comic.title.contains(query, ignoreCase = true)
                }
                _state.update {
                    if (filteredComics.isEmpty()) {
                        ComicsState.Error("No se encontraron comics")
                    } else {
                        ComicsState.Success(filteredComics)
                    }
                }
            }
        }
    }

    fun isLoading(): Boolean = comicsState.value is ComicsState.Loading
    fun totalComics(): Int = _comics.size
}