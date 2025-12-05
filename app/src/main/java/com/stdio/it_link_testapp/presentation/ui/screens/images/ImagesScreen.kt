package com.stdio.it_link_testapp.presentation.ui.screens.images

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.stdio.it_link_testapp.presentation.ui.components.CenteredColumn
import com.stdio.it_link_testapp.presentation.viewmodel.ImagesViewModel

@Composable
fun ImagesScreen(modifier: Modifier = Modifier, onItemClick: (Int) -> Unit) {
    val viewModel = hiltViewModel<ImagesViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val networkIsEnabled by viewModel.networkIsEnabled.collectAsState()
    if (uiState.images.isEmpty() && !networkIsEnabled) {
        CenteredColumn(modifier) {
            Text("Ожидание сети...")
        }
    } else {
        ImageGrid(images = uiState.images, modifier = modifier, onItemClick) {
            viewModel.reloadThumbnail(it)
        }
    }
}