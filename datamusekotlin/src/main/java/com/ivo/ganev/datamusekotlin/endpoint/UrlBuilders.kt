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
package com.ivo.ganev.datamusekotlin.endpoint

import okhttp3.HttpUrl

/**
 * Builds a URL String from the list of [EndpointKeyValue]
 * */
internal class DatamuseUrlBuilder(endpointKeyValues: List<EndpointKeyValue?>, private val path: String) {
    internal companion object {
        const val SCHEME = "https"
        const val AUTHORITY = "api.datamuse.com"
    }

    private val query: List<EndpointKeyValue> = endpointKeyValues.filterNotNull()

    fun build(): String = HttpUrl.Builder().apply {
        scheme(SCHEME)
        host(AUTHORITY)
        addPathSegment(path)
        for (element in query) addQueryParameter(element.key, element.value)
    }.build().toString()
}
