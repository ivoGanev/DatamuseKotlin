package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.internal.HttpClientGet
import com.ivo.ganev.datamusekotlin.internal.HttpResponse
import com.ivo.ganev.datamusekotlin.internal.exceptions.RemoteFailure
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.IOException

class DatamuseOkHttpClientGet : HttpClientGet {
    private val client: OkHttpClient = OkHttpClient()

    override suspend fun get(url: String): HttpResponse<RemoteFailure, String> {
        return getResponseAsync(url).await()
    }

    private fun getResponseAsync(url: String) = GlobalScope.async(Dispatchers.IO) {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use {
           if(!it.isSuccessful)
               return@async HttpResponse.Failure(RemoteFailure.HttpCodeFailure(it.code))

            val result = it.body?.string() ?: ""
            return@async HttpResponse.Result(result)
        }
    }
}