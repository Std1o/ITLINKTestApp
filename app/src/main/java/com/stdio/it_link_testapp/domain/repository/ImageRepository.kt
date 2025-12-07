package com.stdio.it_link_testapp.domain.repository

import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageRepository {
    suspend fun getImages(): String
    fun getThumbnailFile(index: Int): File
    suspend fun loadThumbnail(url: String, index: Int): ImageData<Image>
    suspend fun loadImage(url: String, index: Int): Flow<ImageData<Image>>
    suspend fun reloadThumbnail(url: String, index: Int): Flow<ImageData<Image>>
    fun observeNetworkState(): Flow<Boolean>
}