/**
 * Copyright (C) 2020 Ivo Ganev
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
package com.ivo.ganev.datamuse_kotlin.client

import com.ivo.ganev.datamuse_kotlin.configuration.EndpointConfiguration
import com.ivo.ganev.datamuse_kotlin.response.WordResponse
import com.ivo.ganev.datamuse_kotlin.endpoint.internal.WordResponseDecoder
import com.ivo.ganev.datamuse_kotlin.endpoint.words.*
import com.ivo.ganev.datamuse_kotlin.response.QueryResponse
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure.MalformedJsonBodyFailure
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * [DatamuseKotlinClient]'s purpose is to make queries to it's endpoints and retrieve
 * the results in a [QueryResponse]
 * */
class DatamuseKotlinClient {
    private val wordResponseDecoder = WordResponseDecoder()
    private val httpClient: OkHttpClient = OkHttpClient()

    /**
     * Queries the client for a [QueryResponse]. The response contains
     * either a failure or a result which can be sorted out with [QueryResponse.applyEither]
     * */
    suspend fun query(configuration: EndpointConfiguration) =
        withContext(Dispatchers.IO) {
            val request: Request = Request.Builder()
                .url(configuration.toUrl())
                .build()

            val httpCall = httpClient.newCall(request)
            httpCall.execute().use {
                if (!it.isSuccessful)
                    return@withContext QueryResponse.Failure(RemoteFailure.HttpCodeFailure(it.code))

                val jsonBody = it.body?.string() ?: return@withContext QueryResponse.Failure(MalformedJsonBodyFailure("Json body is null."))
                val jsonBodyResponseSet: Set<WordResponse>

                try {
                    jsonBodyResponseSet = wordResponseDecoder.decode(jsonBody)
                } catch (e: SerializationException) {
                    return@withContext QueryResponse.Failure(MalformedJsonBodyFailure(e.message))
                }
                return@withContext QueryResponse.Result(jsonBodyResponseSet)
            }
        }
}