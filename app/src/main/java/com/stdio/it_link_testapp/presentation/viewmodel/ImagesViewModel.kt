package com.stdio.it_link_testapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {

    private val _uiState = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()

    private val _networkIsEnabled = MutableStateFlow(false)
    val networkIsEnabled = _networkIsEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _uiState.value = repository.getImages()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            repository.observeNetworkState().collect {
                _networkIsEnabled.value = it
                if (uiState.value.isEmpty() && it) {
                    _uiState.value = repository.getImages()
                }
            }
        }
    }
}