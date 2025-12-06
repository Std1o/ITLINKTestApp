package com.stdio.it_link_testapp.presentation.ui.screens.imageDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    if (totalPages > 1) {
        Text(
            text = "${currentPage + 1}/$totalPages",
            color = Color.White,
            fontSize = 16.sp,
            modifier = modifier
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}