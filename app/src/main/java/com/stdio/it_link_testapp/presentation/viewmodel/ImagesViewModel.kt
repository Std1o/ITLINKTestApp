package com.stdio.it_link_testapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import com.stdio.it_link_testapp.domain.usecases.GetThumbnailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val getThumbnailsUseCase: GetThumbnailsUseCase,
) : ViewModel() {

    private val _images = mutableStateListOf<ImageData<Image>>()
    val images: List<ImageData<Image>> = _images

    private val _networkIsEnabled = MutableStateFlow(false)
    val networkIsEnabled = _networkIsEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            loadThumbnails()
            repository.observeNetworkState().collect {
                _networkIsEnabled.value = it
                if (images.isEmpty() && it) {
                    loadThumbnails()
                }
            }
        }
    }

    private fun loadThumbnails() {
        try {
            viewModelScope.launch {
                getThumbnailsUseCase().collect { images ->
                    _images.clear()
                    _images.addAll(images)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reloadThumbnail(url: String, index: Int) {
        viewModelScope.launch {
            repository.reloadThumbnail(url, index).collect { image ->
                _images[index] = image
            }
        }
    }
}