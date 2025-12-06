package com.stdio.it_link_testapp.data.repository

import com.stdio.it_link_testapp.common.utils.NetworkMonitor
import com.stdio.it_link_testapp.data.local.ImagesCacheManager
import com.stdio.it_link_testapp.data.remote.ImageApi
import com.stdio.it_link_testapp.data.remote.ImageLoader
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val networkMonitor: NetworkMonitor,
    private val imageLoader: ImageLoader,
    private val imagesCacheManager: ImagesCacheManager
) : ImageRepository {
    override suspend fun getImages(): String {
        return imagesCacheManager.getCache() ?: imageApi.getImages().also {
            imagesCacheManager.saveCache(it)
        }
    }

    override suspend fun loadThumbnail(
        url: String,
        index: Int
    ) = imageLoader.loadThumbnail(url, index)

    override suspend fun loadImage(
        url: String,
        index: Int
    ) = imageLoader.loadOriginal(url, index)

    override suspend fun reloadThumbnail(
        url: String,
        index: Int
    ): Flow<ImageData<Image>> {
        imageLoader.clearImageCache(index)
        return imageLoader.loadThumbnail(url, index)
    }

    override fun observeNetworkState() = networkMonitor.isOnline
}