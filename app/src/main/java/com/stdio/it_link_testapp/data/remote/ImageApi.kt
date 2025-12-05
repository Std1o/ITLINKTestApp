package com.stdio.it_link_testapp.data.remote

import retrofit2.http.GET

interface ImageApi {
    @GET("test/images.txt")
    suspend fun getImages(): String
}