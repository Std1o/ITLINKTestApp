package com.stdio.it_link_testapp.data.repository

import com.stdio.it_link_testapp.common.utils.NetworkMonitor
import com.stdio.it_link_testapp.data.remote.ImageApi
import com.stdio.it_link_testapp.data.remote.ImageLoader
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val networkMonitor: NetworkMonitor,
    private val imageLoader: ImageLoader
) : ImageRepository {
    override suspend fun getImages() = imageApi.getImages()
    override suspend fun loadThumbnail(
        url: String,
        index: Int
    ) = imageLoader.loadThumbnail(url, index)

    override fun observeNetworkState() = networkMonitor.isOnline
}