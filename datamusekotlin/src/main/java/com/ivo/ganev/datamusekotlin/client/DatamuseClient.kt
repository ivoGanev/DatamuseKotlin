/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivo.ganev.datamusekotlin.client

import com.ivo.ganev.datamusekotlin.configuration.EndpointBuilder
import com.ivo.ganev.datamusekotlin.configuration.UrlConfig
import com.ivo.ganev.datamusekotlin.response.QueryResponse
import com.ivo.ganev.datamusekotlin.response.RemoteFailure
import com.ivo.ganev.datamusekotlin.endpoint.words.DatamuseJsonResponseDecoder
import com.ivo.ganev.datamusekotlin.endpoint.words.KotlinJsonWordDecoder
import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * [DatamuseClient]'s purpose is to make queries to it's endpoints and retrieve
 * the results in a [QueryResponse]
 * */
class DatamuseClient : Client {
    private val decoder: DatamuseJsonResponseDecoder = KotlinJsonWordDecoder()
    private val httpClient: OkHttpClient = OkHttpClient()

    /**
     * Call this method to retrieve a single JSON query from Datamuse.
     * */
    override suspend fun query(builder: EndpointBuilder<UrlConfig>):
            QueryResponse<RemoteFailure, Set<WordResponse>> =
        queryAsync(builder.buildUrl()).await()

    /**
     * Will query the Datamuse API asynchronously
     * */
    private fun queryAsync(url: String) =
        GlobalScope.async(Dispatchers.IO) {
            val request: Request = Request.Builder()
                .url(url)
                .build()

            httpClient.newCall(request).execute().use {
                if (!it.isSuccessful)
                    return@async QueryResponse.Failure(RemoteFailure.HttpCodeFailure(it.code))

                val jsonBody = it.body?.string() ?: throw Exception("Json body is null.")

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