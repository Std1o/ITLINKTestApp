package com.stdio.it_link_testapp.domain.repository

import com.stdio.it_link_testapp.domain.model.ImageData
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImages(): String
    suspend fun loadThumbnail(url: String, index: Int): Flow<ImageData<String>>
    suspend fun loadImage(url: String, index: Int): Flow<ImageData<String>>
    suspend fun reloadThumbnail(url: String, index: Int): Flow<ImageData<String>>
    fun observeNetworkState(): Flow<Boolean>
}