package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.internal.HttpClientGet
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class DatamuseOkHttpClientGet : HttpClientGet {
    private val client: OkHttpClient = OkHttpClient()

    override suspend fun get(url: String): String {
        var result : String = ""
        try {
            result = getAsync(url).await()
        }
        catch(e: IOException) {
            println("Caught")
        }
        return result
    }

    private fun getAsync(url: String) = GlobalScope.async(Dispatchers.IO) {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use {
            if (!it.isSuccessful) throw IOException("Unexpected code $it")
            return@async it.body!!.string()
        }
    }
}