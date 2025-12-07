package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.Image
import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val repository: ImageRepository,
    private val processImageUseCase: ProcessImageUseCase
) {
    operator fun invoke(url: String, index: Int): Flow<ImageData<Image>> {
        val originalFile = repository.getOriginalFile(index)
        return processImageUseCase(url, index, originalFile)
    }
}