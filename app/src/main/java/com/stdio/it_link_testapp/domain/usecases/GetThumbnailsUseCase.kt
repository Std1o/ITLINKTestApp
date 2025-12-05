package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThumbnailsUseCase @Inject constructor(
    private val repository: ImageRepository,
    private val processImagesListUseCase: ProcessImagesListUseCase
) {
    suspend operator fun invoke(): List<Flow<ImageData<String>>> {
        return processImagesListUseCase { url, index ->
            repository.loadThumbnail(url, index)
        }
    }
}