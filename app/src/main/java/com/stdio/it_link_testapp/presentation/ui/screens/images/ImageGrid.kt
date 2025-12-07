package com.stdio.it_link_testapp.presentation.ui.screens.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageGrid(
    images: List<ImageData<Image>>,
    modifier: Modifier,
    onItemClick: (Int) -> Unit,
    onRetry: (String, Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(calculateColumns(100.dp, 120.dp)),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = images,
            key = { index, _ -> index }) { index, item ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable { onItemClick(index) },
                contentAlignment = Alignment.Center
            ) {
                ImageItem(item) { url ->
                    onRetry(url, index)
                }
            }
        }
    }
}