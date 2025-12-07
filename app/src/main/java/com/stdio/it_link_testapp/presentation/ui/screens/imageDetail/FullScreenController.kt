package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun FullScreenController(isFullscreen: Boolean) {
    val view = LocalView.current
    val window = (view.context as? android.app.Activity)?.window
    DisposableEffect(isFullscreen) {
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
            exitFullScreen(window)
        }
        onDispose {
            exitFullScreen(window)
        }
    }
}

fun exitFullScreen(window: Window?) {
    window?.let {
        val windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }
}