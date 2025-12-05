package com.stdio.it_link_testapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.it_link_testapp.data.remote.ImageApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(private val api: ImageApi) : ViewModel() {

    private val _uiState = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = api.getImagesList()
        }
    }
}