package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRawImagesUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(): List<String> {
        val response = withContext(Dispatchers.IO) { repository.getImages() }
        val lines = withContext(Dispatchers.Default) { response.split("\n") }
        return lines
    }
}