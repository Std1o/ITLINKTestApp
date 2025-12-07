package com.stdio.it_link_testapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(private val okHttpClient: OkHttpClient) {

    fun getImage(url: String): Response {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", USER_AGENT)
            .build()

        val response = okHttpClient.newCall(request).execute()
        return response
    }

    companion object {
        private const val USER_AGENT = "Mozilla/5.0"
    }
}