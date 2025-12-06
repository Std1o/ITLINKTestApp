package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.presentation.viewmodel.ImageDetailViewModel

@Composable
fun ImageDetailScreen(index: Int) {
    // Fullscreen
    var isFullscreen by remember { mutableStateOf(false) }
    val view = LocalView.current
    val window = (view.context as? android.app.Activity)?.window
    LaunchedEffect(isFullscreen) {
        if (isFullscreen) {
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                val windowInsetsController =
                    WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        } else {
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(it, true)
                val windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    val viewModel = hiltViewModel<ImageDetailViewModel>()
    val images = viewModel.images
    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { images.size }
    )
    Box(
        modifier = Modifier
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

        PageIndicator(
            currentPage = pagerState.currentPage,
            totalPages = images.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}