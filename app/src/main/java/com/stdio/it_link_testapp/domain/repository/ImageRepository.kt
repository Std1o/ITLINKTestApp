package com.stdio.it_link_testapp.domain.repository

import com.stdio.it_link_testapp.domain.model.ThumbnailData
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImages(): String
    suspend fun loadThumbnail(url: String, index: Int): Flow<ThumbnailData<String>>
    fun observeNetworkState(): Flow<Boolean>
}