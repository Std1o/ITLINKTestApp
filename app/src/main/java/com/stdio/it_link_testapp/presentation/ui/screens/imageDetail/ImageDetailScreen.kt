package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.presentation.viewmodel.ImageDetailViewModel

@Composable
fun ImageDetailScreen(modifier: Modifier, index: Int) {
    var isFullscreen by remember { mutableStateOf(false) }
    FullScreenController(isFullscreen)

    val viewModel = hiltViewModel<ImageDetailViewModel>()
    val images = viewModel.images
    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { images.size }
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { isFullscreen = !isFullscreen }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val image = if (images.size > pageIndex) images[pageIndex] else LoadableData.Loading
            ImagePagerItem(image)
        }

        if (!isFullscreen) {
            ShareButton(
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                images = images,
                pagerState = pagerState
            )
        }

        PageIndicator(
            currentPage = pagerState.currentPage,
            totalPages = images.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}