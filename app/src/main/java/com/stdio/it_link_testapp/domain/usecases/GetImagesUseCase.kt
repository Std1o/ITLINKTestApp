package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.ThumbnailData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class GetImagesUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(maxConcurrent: Int = 5): List<Flow<ThumbnailData<String>>> {
        val response = repository.getImages()
        val lines = response.split("\n")
        val semaphore = Semaphore(maxConcurrent)

        return coroutineScope {
            lines.mapIndexed { index, url ->
                async {
                    semaphore.withPermit {
                        println(repository.loadThumbnail(url, index))
                        repository.loadThumbnail(url, index)
                    }
                }
            }.awaitAll()
        }
    }
}