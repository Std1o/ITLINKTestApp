package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData

@Composable
fun ShareButton(modifier: Modifier, images: List<ImageData<Image>>, pagerState: PagerState) {
    val context = LocalContext.current
    val shareImage = {
        val currentImage = images.getOrNull(pagerState.currentPage)
        if (currentImage is LoadableData.Success) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, currentImage.data.url)
                type = "text/plain"
            }
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.share_image)
                )
            )
        }
    }
    IconButton(
        onClick = shareImage,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Поделиться"
        )
    }
}