package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetThumbnailsUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(): List<Flow<ImageData<String>>> {
        val response = withContext(Dispatchers.IO) { repository.getImages() }
        val lines = withContext(Dispatchers.Default) { response.split("\n") }
        val semaphore = Semaphore(MAX_CONCURRENT)

        return coroutineScope {
            lines.mapIndexed { index, url ->
                async {
                    semaphore.withPermit {
                        repository.loadThumbnail(url, index)
                    }
                }
            }.awaitAll()
        }
    }

    companion object {
        const val MAX_CONCURRENT = 5
    }
}