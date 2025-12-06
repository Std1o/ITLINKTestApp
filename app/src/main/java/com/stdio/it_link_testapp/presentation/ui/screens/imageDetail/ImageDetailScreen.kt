package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.presentation.viewmodel.ImageDetailViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun ImageDetailScreen(index: Int) {
    val viewModel = hiltViewModel<ImageDetailViewModel>()
    val images = viewModel.images
    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { images.size }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val image = if (images.size > pageIndex) images[pageIndex] else LoadableData.Loading
            ImagePagerItem(image)
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