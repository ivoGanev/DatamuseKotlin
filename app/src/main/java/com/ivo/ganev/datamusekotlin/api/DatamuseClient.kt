package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.*
import com.ivo.ganev.datamusekotlin.core.DatamuseJsonResponseDecoder
import com.ivo.ganev.datamusekotlin.core.Client
import com.ivo.ganev.datamusekotlin.core.KotlinJsonWordDecoder
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException

/**
 * Datamuse Client's purpose is to make queries to it's endpoints and retrieve
 * the results in a [QueryResponse]
 * */
class DatamuseClient : Client {
    private val decoder: DatamuseJsonResponseDecoder = KotlinJsonWordDecoder()
    private val httpClient: OkHttpClient = OkHttpClient()

    /**
     * Calling this method will retrieve a single JSON query from Datamuse
     * */
    override suspend fun query(config: WordsEndpointConfigBuilder):
            QueryResponse<RemoteFailure, Set<WordResponse>> = getResponseAsync(config).await()

    private fun getResponseAsync(config: WordsEndpointConfigBuilder) =
        GlobalScope.async(Dispatchers.IO) {
            val request: Request = Request.Builder()
                .url(toWordsEndpointUrl(config))
                .build()

            // I have to decouple the Config to Endpoints builder in order to test this shit

            httpClient.newCall(request).execute().use {
                if (!it.isSuccessful)
                    return@async QueryResponse.Failure(RemoteFailure.HttpCodeFailure(it.code))

                val jsonBody = it.body?.string() ?: throw JSONException("Json body is null.")

                val jsonBodyResponseSet: Set<WordResponse>
                try {
                    jsonBodyResponseSet = decoder.decode(jsonBody)
                } catch (e: SerializationException) {
                    return@async QueryResponse.Failure(RemoteFailure.MalformedJsonBodyFailure(e.message))
                }
                return@async QueryResponse.Result(jsonBodyResponseSet)
            }
        }
}