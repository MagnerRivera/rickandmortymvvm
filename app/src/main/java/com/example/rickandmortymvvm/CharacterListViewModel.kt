package com.example.rickandmortymvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    fun getPageCharacters(page: Int) {
        viewModelScope.launch {
            repository.getPageCharacters(page)
                .catch { it.printStackTrace() }
                .collect {
                    Log.d("CharactersViewModel", "page $page: $it")
                }
        }
    }
}