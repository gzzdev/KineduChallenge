package com.cuzztomgdev.kineduchallenge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.domain.model.Creator
import com.cuzztomgdev.kineduchallenge.domain.usecase.GetCreatorUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicDetailVM @Inject constructor(private val getCreatorUC: GetCreatorUC): ViewModel() {
    private var _state = MutableStateFlow<CreatorState>(CreatorState.Start)
    lateinit var comic: Comic
    val creatorsState: StateFlow<CreatorState> = _state

    fun creatorInfo() {
        viewModelScope.launch {
            _state.update { CreatorState.Loading }
            val creator = getCreatorUC(comic.creators.first())
            if (creator != null) {
                _state.update { CreatorState.Success(creator) }
            } else {
                _state.update { CreatorState.Error("Información del creador no disponible") }
            }
        }
    }
    fun isLoading(): Boolean = creatorsState.value is CreatorState.Loading
}