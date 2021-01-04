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
import com.ivo.ganev.datamusekotlin.exceptions.UnspecifiedHardConstraintException
import java.util.*

/**
 * A [ConfigurationBuilder] is used to create a [WordsEndpointConfig]
 * which is made of [EndpointKeyValue]'s. It's basically an easy
 * way to map user defined primitive-type options to [EndpointKeyValue]'s.
 * */
interface ConfigurationBuilder {
    fun build(): WordsEndpointConfig
}

/**
 * The [WordsEndpointBuilder] will build [WordsEndpointConfig] for the
 * words/ endpoint. Usage: [buildWordsEndpointUrl]
 * */
class WordsEndpointBuilder() : ConfigurationBuilder, UrlConvertible {


    /**
     * Set this to provide a hard constraint
     * @see [HardConstraint]
     * */
    var hardConstraint: HardConstraint? = null

    /**
     * A query element for the topics
     * @see [Topic]
     * */
    var topics: String? = null

    /**
     * A query element for the left context
     * @see  [LeftContext]
     * */
    var leftContext: String? = null

    /**
     * A query element for the right context
     * @see  [RightContext]
     * */
    var rightContext: String? = null

    /**
     * A query element for the max results
     * @see  [MaxResults]
     * */
    var maxResults: Int? = null

    /**
     * @see  [Metadata]
     * */
    var metadata: EnumSet<MetadataFlag>? = null

    override fun build(): WordsEndpointConfig {
        return WordsEndpointConfig(
            hardConstraint,
            topics?.let { Topic(it) },
            leftContext?.let { LeftContext(it) },
            rightContext?.let { RightContext(it) },
            maxResults?.let { MaxResults(it) },
            metadata?.let { Metadata(it) }
        )
    }

    override fun toUrl(): String = ConfigurationToURLConverter.Default.from(this.build())
}

/**
 * Builds the URL address for the Datamuse API. In order to be able to build a URL you need to
 * provide at least a hard constraint.
 *
 *
 * Usage:
 *
 * ```
 * buildWordsEndpointUrl {
 *  hardConstraint = HardConstraint.MeansLike("elephant")
 *  topics = "first,second,third,fourth,fifth"
 *  leftContext = "big"
 *  rightContext = "ears"
 *  maxResults = 10
 *  metadata = MetadataFlag.DEFINITIONS and Metadata.Flag.PARTS_OF_SPEECH
 * }
 * ```
 * @throws UnspecifiedHardConstraintException - when no hard constraint is specified.
 * Reason: An endpoint with no hard constraint will yield no result.
 * */
fun buildWordsEndpointUrl(wordsConfig: WordsEndpointBuilder.() -> Unit):
        WordsEndpointBuilder {
    val builder = WordsEndpointBuilder()
    builder.wordsConfig()
    if (builder.hardConstraint == null)
        throw UnspecifiedHardConstraintException(
            "You need to provide a hard constraint in order to build a URL for the API"
        )
    return builder
}