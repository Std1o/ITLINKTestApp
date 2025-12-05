// ConvertImagesFlowUseCase.kt
package com.stdio.it_link_testapp.domain.usecases

import com.stdio.it_link_testapp.domain.model.ImageData
import com.stdio.it_link_testapp.domain.model.LoadableData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConvertImagesFlowUseCase @Inject constructor() {
    suspend operator fun invoke(flows: List<Flow<ImageData<String>>>): Flow<List<ImageData<String>>> =
        coroutineScope {
            flow {
                val result = MutableList<ImageData<String>>(flows.size) { LoadableData.Loading }

                flows.forEachIndexed { index, flow ->
                    flow.collect { imageData ->
                        result[index] = imageData
                        emit(result)
                    }
                }
            }
        }
}