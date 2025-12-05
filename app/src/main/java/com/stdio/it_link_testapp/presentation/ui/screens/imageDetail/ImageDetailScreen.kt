package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.presentation.viewmodel.ImageDetailViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun ImageDetailScreen(index: Int) {
    val viewModel = hiltViewModel<ImageDetailViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val image = if (uiState.size > index) uiState[index] else flowOf(LoadableData.Loading)
    val imageState by image.collectAsState(LoadableData.Loading)
    ImagePagerItem(imageState)
}