package com.stdio.it_link_testapp.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.domain.model.ThumbnailData
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
        File(context.cacheDir, "images").apply {
            if (!exists()) mkdirs()
        }
    }

    fun loadThumbnail(url: String, index: Int): Flow<ThumbnailData<String>> =
        flow<ThumbnailData<String>> {
            emit(LoadableData.Loading)
            try {
                val thumbnailFile = getThumbnailFile(index)

                // Проверяем файловый кэш
                if (thumbnailFile.exists() && thumbnailFile.length() > 0) {
                    emit(LoadableData.Success(thumbnailFile.absolutePath))
                }

                // Загружаем через OkHttp (уже с кэшированием от OkHttp)
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = okHttpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    emit(
                        LoadableData.Error(
                            exception = response.message,
                            code = response.code
                        )
                    )
                }

                response.body?.use { body ->
                    // Создаем миниатюру
                    body.byteStream().use { inputStream ->
                        val options = BitmapFactory.Options().apply {
                            inSampleSize = 4 // Уменьшаем в 4 раза для миниатюры
                        }

                        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                        if (bitmap == null) {
                            emit(LoadableData.Error("Failed to decode image"))
                        }

                        try {
                            // Сохраняем миниатюру
                            FileOutputStream(thumbnailFile).use { fos ->
                                bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                            }

                            emit(LoadableData.Success(thumbnailFile.absolutePath))
                        } finally {
                            bitmap?.recycle()
                        }
                    }
                } ?: emit(LoadableData.Error("Empty response body"))

            } catch (e: Exception) {
                emit(LoadableData.Error(e.message ?: "Unknown error"))
            }
        }.flowOn(Dispatchers.IO)

    private fun getThumbnailFile(index: Int): File {
        return File(imageCacheDir, "thumb_$index.jpg")
    }
}