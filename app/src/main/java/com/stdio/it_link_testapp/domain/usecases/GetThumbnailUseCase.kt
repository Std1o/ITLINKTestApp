package com.stdio.it_link_testapp.domain.usecases

import android.content.Context
import com.stdio.it_link_testapp.R
import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetThumbnailUseCase @Inject constructor(
    private val repository: ImageRepository,
    private val context: Context
) {
    operator fun invoke(url: String, index: Int): Flow<ImageData<Image>> =
        flow {
            emit(LoadableData.Loading)
            try {
                val thumbnailFile = repository.getThumbnailFile(index)

                // Проверяем файловый кэш
                if (thumbnailFile.exists() && thumbnailFile.length() > 0) {
                    emit(LoadableData.Success(Image(path = thumbnailFile.absolutePath, url = url)))
                    return@flow
                }

                if (!url.startsWith(HTTPS_SUFFIX)) {
                    emit(ImageData.Placeholder)
                    return@flow
                }

                emit(repository.loadThumbnail(url, index))
            } catch (e: Exception) {
                emit(
                    LoadableData.Error(
                        exception = e.message ?: context.getString(R.string.unknown_error),
                        url = url
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    companion object {
        private const val HTTPS_SUFFIX = "https"
    }
}