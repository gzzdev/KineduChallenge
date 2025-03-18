package com.cuzztomgdev.kineduchallenge.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.domain.model.Creator
import com.cuzztomgdev.kineduchallenge.domain.usecase.GetComicsByIdsUC
import com.cuzztomgdev.kineduchallenge.domain.usecase.GetCreatorUC
import com.cuzztomgdev.kineduchallenge.ui.common.state.ComicsState
import com.cuzztomgdev.kineduchallenge.ui.common.state.CreatorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicDetailVM @Inject constructor(
    private val getCreatorUC: GetCreatorUC,
    private val getComicsByIdsUC: GetComicsByIdsUC
): ViewModel() {

    private var _state = MutableStateFlow<CreatorState>(CreatorState.Start)
    lateinit var comic: Comic
    val creatorsState: StateFlow<CreatorState> = _state

    private var _comicState = MutableStateFlow<ComicsState>(ComicsState.Start)
    val comicsState: StateFlow<ComicsState> = _comicState

    fun creatorInfo() {
        viewModelScope.launch {
            _state.update { CreatorState.Loading }
            val creator = getCreatorUC(comic.creators.first())
            if (creator != null) {
                _state.update { CreatorState.Success(creator) }
                getComics(creator.comics)
            } else {
                _state.update { CreatorState.Error("Creator data not found") }
            }
        }
    }

    fun getComics(comicsIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            _comicState.value = ComicsState.Loading
            Log.i("ComicDetailVM", "getComics: $comicsIds")
            // Secondary Thread
            if (comicsIds.isEmpty()) {
                _comicState.value = ComicsState.Error("Comics not found")
                return@launch
            }

            val newComics = getComicsByIdsUC(comicsIds)
            if (newComics.isEmpty()) {
                _comicState.value = ComicsState.Error("Comics not found")
            } else {
                val uniqueComics = newComics.filter { newComic ->
                    newComic.id != comic.id
                }
                _comicState.value = ComicsState.Success(newComics.toList())
            }
        }
    }
}