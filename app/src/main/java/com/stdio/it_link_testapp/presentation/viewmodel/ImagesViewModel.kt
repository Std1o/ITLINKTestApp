package com.stdio.it_link_testapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import com.stdio.it_link_testapp.domain.usecases.GetRawImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val getRawImagesUseCase: GetRawImagesUseCase,
) : ViewModel() {

    private val _images = mutableStateListOf<ImageData<Image>>()
    val images: List<ImageData<Image>> = _images

    private val _networkIsEnabled = MutableStateFlow(false)
    val networkIsEnabled = _networkIsEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeNetworkState().collect {
                _networkIsEnabled.value = it
                if (images.isEmpty() && it) {
                    loadThumbnails()
                }
            }
        }
    }

    private suspend fun loadThumbnails() {
        try {
            _images.clear()
            val rawImages = getRawImagesUseCase()
            repeat(rawImages.size) { _images.add(LoadableData.Loading) }

            rawImages.forEachIndexed { index, _ ->
                repository.loadThumbnail(rawImages[index], index).collect {
                    _images[index] = it
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