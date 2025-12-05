package com.stdio.it_link_testapp.presentation.ui.screens.images

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.presentation.ui.components.CenteredColumn

@Composable
fun ImageItem(imageState: ImageData<String>, onRetry: () -> Unit) {
    when (imageState) {
        is LoadableData.Error -> {
            CenteredColumn {
                Icon(Icons.Default.Error, "Error")
                OutlinedButton(modifier = Modifier.padding(top = 8.dp), onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }

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