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
import com.ivo.ganev.datamusekotlin.configuration.WordsEndpointBuilder
import com.ivo.ganev.datamusekotlin.response.QueryResponse
import com.ivo.ganev.datamusekotlin.response.RemoteFailure
import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse

internal interface Client {
   suspend fun query(builder: EndpointBuilder<UrlConfig>) :
           QueryResponse<RemoteFailure, Set<WordResponse>>
}

interface UrlConvertible {
    fun toUrl(): String
}