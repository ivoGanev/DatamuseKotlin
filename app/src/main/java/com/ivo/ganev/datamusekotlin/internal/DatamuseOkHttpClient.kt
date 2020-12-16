package com.ivo.ganev.datamusekotlin.internal

import com.ivo.ganev.datamusekotlin.internal.failure.RemoteFailure
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException

internal class DatamuseOkHttpClient(private val decoder: DatamuseJsonResponseDecoder) : HttpClientGet {
    private val client: OkHttpClient = OkHttpClient()

    override suspend fun get(url: String): HttpResponse<RemoteFailure, Set<WordResponse>> {
        return getResponseAsync(url).await()
    }

    private fun getResponseAsync(url: String) = GlobalScope.async(Dispatchers.IO) {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use {
           if(!it.isSuccessful)
               return@async HttpResponse.Failure(RemoteFailure.HttpCodeFailure(it.code))

            val jsonBody = it.body?.string() ?: throw JSONException("Json body is null.")

            val jsonBodyResponseSet: Set<WordResponse>
            try {
                 jsonBodyResponseSet  = decoder.decode(jsonBody)
            }
            catch (e: SerializationException) {
                return@async HttpResponse.Failure(RemoteFailure.MalformedJsonBodyFailure(e.message))
            }
            return@async HttpResponse.Result(jsonBodyResponseSet)
        }
    }
}