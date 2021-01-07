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
package com.ivo.ganev.datamuse_kotlin.configuration

import okhttp3.HttpUrl

/**
 * Converts an endpoint configuration into a URL String
 * */
class ConfigurationConverter {
    companion object {
        fun convert(queryConfig: QueryConfig): String {
            val endpointKeyValue = queryConfig.getQuery()

            return HttpUrl.Builder().apply {
                scheme(queryConfig.scheme)
                host(queryConfig.authority)
                addPathSegment(queryConfig.path)
                for (element in endpointKeyValue.filterNotNull()) addQueryParameter(element.key, element.value)
            }.toString()
        }
    }
}