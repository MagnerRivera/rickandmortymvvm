package com.example.rickandmortymvvm.viewmodel;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.PageCharacters
import com.example.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    /*Backing property to avoid state updates from other classes*/
    private val _uiState: MutableStateFlow<CharacterListUiState> =
        MutableStateFlow(CharacterListUiState.Loading())

    private val page: MutableLiveData<PageCharacters> = MutableLiveData()

    /*La interfaz de usuario recopila de este StateFlow para obtener sus actualizaciones de estado*/
    val uiState: StateFlow<CharacterListUiState> = _uiState

    init {
        getPageCharacters(1)
    }

    private fun getPageCharacters(pageId: Int) {
        viewModelScope.launch {
            repository.getPageCharacters(pageId).flowOn(Dispatchers.IO)
                .onStart { _uiState.value = CharacterListUiState.Loading() }
                .catch {
                    _uiState.value = CharacterListUiState.Loading(false)
                    _uiState.value = CharacterListUiState.Error(it)
                }
                .collect {
                    _uiState.value = CharacterListUiState.Loading(false)
                    page.value = it
                    _uiState.value = CharacterListUiState.Success(it)
                }
        }
    }

    fun before() {
        if (_uiState.value !is CharacterListUiState.Loading) {
            page.value?.info?.prev?.let { getPageCharacters(it) }
        }
    }

    fun next() {
        if (_uiState.value !is CharacterListUiState.Loading) {
            page.value?.info?.next?.let { getPageCharacters(it) }
        }
    }

    /**
     * Representa diferentes estados para la pantalla [CharacterListUiState]
     */
    sealed class CharacterListUiState {
        data class Loading(val isLoading: Boolean = true) : CharacterListUiState()
        data class Success(val page: PageCharacters) : CharacterListUiState()
        data class Error(val exception: Throwable) : CharacterListUiState()
    }
}