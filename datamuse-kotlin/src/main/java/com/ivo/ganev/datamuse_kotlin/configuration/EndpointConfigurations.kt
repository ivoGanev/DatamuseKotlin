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
package com.ivo.ganev.datamuse_kotlin.configuration

import com.ivo.ganev.datamuse_kotlin.endpoint.internal.EndpointKeyValue
import com.ivo.ganev.datamuse_kotlin.endpoint.sug.Hint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.*

abstract class EndpointConfiguration {
    val scheme = "https"
    val authority = "api.datamuse.com"

    abstract val path: String
    internal abstract fun getQuery(): List<EndpointKeyValue?>

    open fun toUrl(): String {
        return ConfigurationConverter.convert(this)
    }
}

open class SugConfiguration(
    @JvmField val hint: Hint? = null,
    @JvmField val maxResults: MaxResults? = null
) : EndpointConfiguration() {
    override val path: String
        get() = "sug"

    override fun getQuery(): List<EndpointKeyValue?> {
        return listOf(hint, maxResults)
    }
}

open class WordsConfiguration(
    @JvmField val hardConstraints: List<HardConstraint?> = emptyList(),
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
) : EndpointConfiguration() {

    override val path: String
        get() = "words"

    override fun getQuery(): List<EndpointKeyValue?> {
        return listOf(*hardConstraints.toTypedArray(), topic, leftContext, rightContext, maxResults, metadata)
    }
}

