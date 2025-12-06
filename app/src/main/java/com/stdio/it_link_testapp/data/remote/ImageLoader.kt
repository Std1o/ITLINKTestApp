package com.stdio.it_link_testapp.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader @Inject constructor(
    private val context: Context,
    private val okHttpClient: OkHttpClient
) {

    private val imageCacheDir by lazy {
        File(context.cacheDir, IMAGES_DIR).apply {
            if (!exists()) mkdirs()
        }
    }

    fun loadThumbnail(url: String, index: Int): Flow<ImageData<Image>> =
        flow {
            emit(LoadableData.Loading)
            try {
                val thumbnailFile = getThumbnailFile(index)

                // Проверяем файловый кэш
                if (thumbnailFile.exists() && thumbnailFile.length() > 0) {
                    emit(LoadableData.Success(Image(path = thumbnailFile.absolutePath, url = url)))
                    return@flow
                }

                if (!url.startsWith(HTTPS_SUFFIX)) {
                    emit(ImageData.Placeholder)
                    return@flow
                }

                // Загружаем через OkHttp
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", USER_AGENT)
                    .build()

                val response = okHttpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    emit(
                        LoadableData.Error(
                            exception = response.message,
                            code = response.code,
                            url = url
                        )
                    )
                    return@flow
                }

                val contentType = response.header("Content-Type", "")?.lowercase() ?: ""
                val isImage = contentType.startsWith(IMAGE_CONTENT_TYPE)
                if (!isImage) {
                    emit(ImageData.Placeholder)
                    return@flow
                }

                response.body?.use { body ->
                    // Создаем миниатюру
                    body.byteStream().use { inputStream ->
                        val options = BitmapFactory.Options().apply {
                            inSampleSize = 4 // Уменьшаем в 4 раза для миниатюры
                        }

                        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                        if (bitmap == null) {
                            emit(
                                LoadableData.Error(
                                    exception = context.getString(R.string.failed_to_decode_image),
                                    url = url
                                )
                            )
                            return@flow
                        }

                        try {
                            // Сохраняем миниатюру
                            FileOutputStream(thumbnailFile).use { fos ->
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                            }

                            emit(
                                LoadableData.Success(
                                    Image(path = thumbnailFile.absolutePath, url = url)
                                )
                            )
                        } finally {
                            bitmap.recycle()
                        }
                    }
                } ?: emit(
                    LoadableData.Error(
                        exception = context.getString(R.string.empty_response_body),
                        url = url
                    )
                )

            } catch (e: Exception) {
                emit(
                    LoadableData.Error(
                        exception = e.message ?: context.getString(R.string.unknown_error),
                        url = url
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    private fun getThumbnailFile(index: Int): File {
        return File(imageCacheDir, "thumb_$index.jpg")
    }

    fun loadOriginal(url: String, index: Int): Flow<ImageData<Image>> =
        flow {
            try {
                val originalFile = getOriginalFile(index)

                // Проверяем файловый кэш
                if (originalFile.exists() && originalFile.length() > 0) {
                    emit(LoadableData.Success(Image(path = originalFile.absolutePath, url = url)))
                    return@flow
                }

                if (!url.startsWith(HTTPS_SUFFIX)) {
                    emit(ImageData.Placeholder)
                    return@flow
                }

                // Загружаем через OkHttp
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", USER_AGENT)
                    .build()

                val response = okHttpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    emit(
                        LoadableData.Error(
                            exception = response.message,
                            code = response.code,
                            url = url
                        )
                    )
                    return@flow
                }

                val contentType = response.header("Content-Type", "")?.lowercase() ?: ""
                val isImage = contentType.startsWith(IMAGE_CONTENT_TYPE)
                if (!isImage) {
                    emit(ImageData.Placeholder)
                    return@flow
                }

                response.body?.use { body ->
                    originalFile.outputStream().use { output ->
                        body.byteStream().copyTo(output)
                    }

                    emit(LoadableData.Success(Image(path = originalFile.absolutePath, url = url)))
                } ?: emit(
                    LoadableData.Error(
                        exception = context.getString(R.string.empty_response_body),
                        url = url
                    )
                )

            } catch (e: Exception) {
                emit(
                    LoadableData.Error(
                        exception = e.message ?: context.getString(R.string.unknown_error),
                        url = url
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    private fun getOriginalFile(index: Int): File {
        return File(imageCacheDir, "original_$index.jpg")
    }

    fun clearImageCache(index: Int) {
        getThumbnailFile(index).delete()
        getOriginalFile(index).delete()
    }

    companion object {
        private const val USER_AGENT = "Mozilla/5.0"
        private const val IMAGE_CONTENT_TYPE = "image/"
        private const val HTTPS_SUFFIX = "https"
        private const val IMAGES_DIR = "images"
    }
}