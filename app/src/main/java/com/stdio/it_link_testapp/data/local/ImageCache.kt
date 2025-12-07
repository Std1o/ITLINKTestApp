package com.stdio.it_link_testapp.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class ImageCache @Inject constructor(private val context: Context) {
    private val imageCacheDir by lazy {
        File(context.cacheDir, IMAGES_DIR).apply {
            if (!exists()) mkdirs()
        }
    }

    fun getThumbnailFile(index: Int): File {
        return File(imageCacheDir, "thumb_$index.jpg")
    }

    fun getOriginalFile(index: Int): File {
        return File(imageCacheDir, "original_$index.jpg")
    }

    fun saveThumbnail(url: String, index: Int, inputStream: InputStream): ImageData<Image> {
        val thumbnailFile = getThumbnailFile(index)
        inputStream.use { inputStream ->
            val options = BitmapFactory.Options().apply {
                inSampleSize = 4 // Уменьшаем в 4 раза для миниатюры
            }

            val bitmap =
                BitmapFactory.decodeStream(inputStream, null, options) ?: return LoadableData.Error(
                    exception = context.getString(R.string.failed_to_decode_image),
                    url = url
                )

            try {
                // Сохраняем миниатюру
                FileOutputStream(thumbnailFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                }

                return LoadableData.Success(
                    Image(path = thumbnailFile.absolutePath, url = url)
                )
            } finally {
                bitmap.recycle()
            }
        }
    }

    fun saveOriginal(url: String, index: Int, inputStream: InputStream): ImageData<Image> {
        val originalFile = getOriginalFile(index)
        originalFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return LoadableData.Success(Image(path = originalFile.absolutePath, url = url))
    }

    fun clearImageCache(index: Int) {
        getThumbnailFile(index).delete()
        getOriginalFile(index).delete()
    }

    companion object {
        private const val IMAGES_DIR = "images"
    }
}