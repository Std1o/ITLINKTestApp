package com.stdio.it_link_testapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImages(): String
    fun observeNetworkState(): Flow<Boolean>
}