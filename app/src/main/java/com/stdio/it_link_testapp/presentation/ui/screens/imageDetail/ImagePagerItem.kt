package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.presentation.ui.components.CenteredColumn
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ImagePagerItem(imageState: ImageData<Image>) {
    when (imageState) {
        is LoadableData.Error -> Image(R.drawable.no_image)
        is LoadableData.Loading -> {
            CenteredColumn {
                CircularProgressIndicator()
            }
        }

        is LoadableData.Success -> Image(imageState.data.path)
        is ImageData.Placeholder -> Image(R.drawable.no_image)
    }
}

@Composable
fun Image(model: Any?) {
    val zoomState = rememberZoomState(
        maxScale = 5f
    )
    AsyncImage(
        model = model,
        contentDescription = "Image",
        modifier = Modifier
            .fillMaxSize()
            .zoomable(zoomState),
        contentScale = ContentScale.Fit
    )
}