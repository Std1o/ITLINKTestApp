package com.stdio.it_link_testapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import com.stdio.it_link_testapp.domain.usecases.GetImagesUseCase
import com.stdio.it_link_testapp.domain.usecases.GetThumbnailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
) : ViewModel() {

    private val _images = mutableStateListOf<ImageData<String>>()
    val images: List<ImageData<String>> = _images

    init {
        viewModelScope.launch {
            try {
                getImagesUseCase().collect { images ->
                    _images.clear()
                    _images.addAll(images)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}