package com.stdio.it_link_testapp.data.local

import android.content.Context
import java.io.File
import javax.inject.Inject

class FileCache @Inject constructor(context: Context) {
    private val cacheFile = File(context.cacheDir, "images_cache.txt")

    fun getCache(): String? {
        if (!cacheFile.exists()) return null
        return cacheFile.readText()
    }

    fun saveCache(content: String) {
        cacheFile.writeText(content)
    }
}
