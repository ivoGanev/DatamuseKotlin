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
package com.ivo.ganev.datamusekotlin.configuration

import com.ivo.ganev.datamusekotlin.client.UrlConvertible
import com.ivo.ganev.datamusekotlin.endpoint.EndpointKeyValue
import com.ivo.ganev.datamusekotlin.endpoint.words.*

abstract class UrlConfig:  UrlConvertible  {
    val scheme = "https"
    val authority = "api.datamuse.com"

    abstract val path: String
    internal abstract fun getQuery(): List<EndpointKeyValue?>

    override fun toUrl(): String {
        return ConfigurationConverter.convert(this)
    }
}

data class WordsEndpointUrlConfig(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
) : UrlConfig(){
    override val path: String
        get() = "words"

    override fun getQuery(): List<EndpointKeyValue?> {
        return listOf(hardConstraint, topic, leftContext, rightContext, maxResults, metadata)
    }
}

