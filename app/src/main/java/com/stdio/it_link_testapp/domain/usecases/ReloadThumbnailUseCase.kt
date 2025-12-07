package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReloadThumbnailUseCase @Inject constructor(
    private val getThumbnailUseCase: GetThumbnailUseCase,
    private val repository: ImageRepository
) {
    operator fun invoke(url: String, index: Int): Flow<ImageData<Image>> {
        repository.clearImageCache(index)
        return getThumbnailUseCase(url, index)
    }
}