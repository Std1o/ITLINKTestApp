package com.stdio.it_link_testapp.presentation.ui.screens.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun calculateColumns(min: Dp, max: Dp): Int {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val columns by remember(screenWidthDp) {
        derivedStateOf {
            val screenWidth = screenWidthDp.dp
            val columnsCalc = (screenWidth / max).toInt().coerceAtLeast(1)
            val actualCellWidth = screenWidth / columnsCalc
            if (actualCellWidth < min) {
                (screenWidth / min).toInt().coerceAtLeast(1)
            } else {
                columnsCalc
            }
        }
    }
    return columns
}