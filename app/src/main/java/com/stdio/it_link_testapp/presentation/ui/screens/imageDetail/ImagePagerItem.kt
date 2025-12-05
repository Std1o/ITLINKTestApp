package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData

@Composable
fun ImagePagerItem(imageState: ImageData<String>) {
    when (imageState) {
        is LoadableData.Error -> Text(imageState.exception)
        is LoadableData.Loading -> CircularProgressIndicator()
        is LoadableData.Success -> {
            AsyncImage(
                model = imageState.data,
                contentDescription = "Thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        is ImageData.Placeholder -> {
            AsyncImage(
                model = R.drawable.no_image,
                contentDescription = "Thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}