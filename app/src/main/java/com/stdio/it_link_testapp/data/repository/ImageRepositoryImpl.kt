package com.stdio.it_link_testapp.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.common.utils.NetworkMonitor
import com.stdio.it_link_testapp.data.local.ImageCache
import com.stdio.it_link_testapp.data.local.ImagesCacheManager
import com.stdio.it_link_testapp.data.remote.ImageApi
import com.stdio.it_link_testapp.data.remote.ImageLoader
import com.stdio.it_link_testapp.data.remote.ImageRemoteDataSource
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import java.io.FileOutputStream
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val networkMonitor: NetworkMonitor,
    private val imageLoader: ImageLoader,
    private val imagesCacheManager: ImagesCacheManager,
    private val imageCache: ImageCache,
    private val remoteDataSource: ImageRemoteDataSource,
    private val context: Context
) : ImageRepository {
    override suspend fun getImages(): String {
        return imagesCacheManager.getCache() ?: imageApi.getImages().also {
            imagesCacheManager.saveCache(it)
        }
    }

    override fun getThumbnailFile(index: Int) = imageCache.getThumbnailFile(index)
    override fun clearImageCache(index: Int) = imageCache.clearImageCache(index)

    override suspend fun loadThumbnail(
        url: String,
        index: Int
    ): ImageData<Image> {
        val response = remoteDataSource.getThumbnail(url)

        if (!response.isSuccessful) {
            return LoadableData.Error(
                exception = response.message,
                code = response.code,
                url = url
            )
        }

        val contentType = response.header("Content-Type", "")?.lowercase() ?: ""
        val isImage = contentType.startsWith(IMAGE_CONTENT_TYPE)
        if (!isImage) {
            return ImageData.Placeholder
        }

        response.body?.use { body ->
            return imageCache.saveThumbnail(url, index, body.byteStream())
        }
        return LoadableData.Error(
            exception = context.getString(R.string.empty_response_body),
            url = url
        )
    }

    override suspend fun loadImage(
        url: String,
        index: Int
    ) = imageLoader.loadOriginal(url, index)

    override fun observeNetworkState() = networkMonitor.isOnline

    companion object {
        private const val IMAGE_CONTENT_TYPE = "image/"
    }
}