package com.stdio.it_link_testapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import com.stdio.it_link_testapp.domain.usecases.GetThumbnailsUseCase
import com.stdio.it_link_testapp.domain.usecases.ReloadThumbnailUseCase
import com.stdio.it_link_testapp.presentation.model.ImagesUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val getThumbnailsUseCase: GetThumbnailsUseCase,
    private val reloadThumbnailUseCase: ReloadThumbnailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImagesUIState())
    val uiState = _uiState.asStateFlow()

    private val _networkIsEnabled = MutableStateFlow(false)
    val networkIsEnabled = _networkIsEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            loadThumbnails()
            repository.observeNetworkState().collect {
                _networkIsEnabled.value = it
                if (uiState.value.images.isEmpty() && it) {
                    loadThumbnails()
                }
            }
        }
    }

    private fun loadThumbnails() {
        try {
            viewModelScope.launch {
                getThumbnailsUseCase().collect { images ->
                    _uiState.update { it.copy(images = images.toList()) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reloadThumbnail(index: Int) {
        viewModelScope.launch {
            val list = uiState.value.images.toMutableList()
            reloadThumbnailUseCase(index).collect { image ->
                list[index] = image
                _uiState.update { it.copy(images = list.toList()) }
            }
        }
    }
}