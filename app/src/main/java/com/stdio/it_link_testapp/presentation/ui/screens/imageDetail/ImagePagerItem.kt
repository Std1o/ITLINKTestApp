package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.smarttoolfactory.zoom.rememberZoomState
import com.smarttoolfactory.zoom.zoom
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData

@Composable
fun ImagePagerItem(imageState: ImageData<String>) {
    when (imageState) {
        is LoadableData.Error -> Text(imageState.exception)
        is LoadableData.Loading -> CircularProgressIndicator()
        is LoadableData.Success -> Image(imageState.data)
        is ImageData.Placeholder -> Image(R.drawable.no_image)
    }
}

@Composable
fun Image(model: Any?) {
    val zoomState = rememberZoomState(
        minZoom = 1f,
        maxZoom = 5f,
        zoomable = true,
        pannable = true,
        limitPan = true,
    )
    AsyncImage(
        model = model,
        contentDescription = "Image",
        modifier = Modifier.fillMaxSize().zoom(zoomState = zoomState),
        contentScale = ContentScale.Crop
    )
}