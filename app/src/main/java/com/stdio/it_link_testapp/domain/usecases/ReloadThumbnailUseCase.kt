package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReloadThumbnailUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(index: Int): Flow<ImageData<String>> {
        val response = withContext(Dispatchers.IO) { repository.getImages() }
        val lines = withContext(Dispatchers.Default) { response.split("\n") }
        val url = lines[index]
        return repository.reloadThumbnail(url, index)
    }
}