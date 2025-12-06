package com.stdio.it_link_testapp.presentation.ui.screens.images

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData

@Composable
fun ImageItem(imageState: ImageData<Image>, onRetry: () -> Unit) {
    when (imageState) {
        is LoadableData.Error -> {
            Box {
                Image(R.drawable.no_image)
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.BottomCenter)
                        .size(60.dp, 20.dp)
                        .clickable(onClick = onRetry)
                ) {
                    Text(
                        text = stringResource(R.string.retry),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        is LoadableData.Loading -> CircularProgressIndicator()
        is LoadableData.Success -> Image(imageState.data.path)
        is ImageData.Placeholder -> Image(R.drawable.no_image)
    }
}

@Composable
fun Image(model: Any?) {
    AsyncImage(
        model = model,
        contentDescription = "Thumbnail",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}