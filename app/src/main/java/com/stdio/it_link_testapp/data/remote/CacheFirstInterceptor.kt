package com.stdio.it_link_testapp.data.remote

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheFirstInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Сначала пробуем получить из кэша
        val cacheControl = CacheControl.Builder()
            .maxStale(Int.MAX_VALUE, TimeUnit.SECONDS)
            .build()

        val cacheRequest = request.newBuilder()
            .cacheControl(cacheControl)
            .build()

        val cacheResponse = chain.proceed(cacheRequest)

        if (cacheResponse.code == 504) { // Если кэш пустой или просрочен
            cacheResponse.close()

            // Тогда идем в сеть
            val networkControl = CacheControl.Builder()
                .maxAge(0, TimeUnit.SECONDS)
                .build()

            val networkRequest = request.newBuilder()
                .cacheControl(networkControl)
                .build()

            return chain.proceed(networkRequest)
        }

        return cacheResponse
    }
}