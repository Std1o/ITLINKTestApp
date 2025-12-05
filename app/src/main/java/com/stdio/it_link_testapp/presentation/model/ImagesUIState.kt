package com.stdio.it_link_testapp.presentation.model

import com.stdio.it_link_testapp.domain.model.ImageData

data class ImagesUIState(val images: List<ImageData<String>> = emptyList())
