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

import com.ivo.ganev.datamuse_kotlin.endpoint.words.*
import com.ivo.ganev.datamuse_kotlin.exceptions.IllegalHardConstraintState
import java.util.*

abstract class EndpointBuilder<out T : QueryConfig>
{
    abstract fun build() : T

    fun buildUrl() : String = build().toUrl()
}
/**
 * The [WordsEndpointBuilder] will build [WordsEndpointQueryConfig] for the
 * words/ endpoint. Usage: [buildWordsEndpointUrl]
 * */
class WordsEndpointBuilder : EndpointBuilder<WordsEndpointQueryConfig>() {

    /**
     * Set this to provide a hard constraint
     * @see [HardConstraint]
     * */
    var hardConstraints: List<HardConstraint?> = emptyList()

    /**
     * Set this to add topics to the query
     * @see [Topic]
     * */
    var topics: String? = null

    /**
     * Set this to add left context to the query
     * @see  [LeftContext]
     * */
    var leftContext: String? = null

    /**
     * Set this to add right context to the query
     * @see  [RightContext]
     * */
    var rightContext: String? = null

    /**
     * Set this to set the maximum results for the query
     * @see  [MaxResults]
     * */
    var maxResults: Int? = null

    /**
     * Set this to add metadata flags to the query
     * @see  [Metadata]
     * */
    var metadata: EnumSet<MetadataFlag>? = null

     override fun build(): WordsEndpointQueryConfig {
        return WordsEndpointQueryConfig(
            hardConstraints,
            topics?.let { Topic(it) },
            leftContext?.let { LeftContext(it) },
            rightContext?.let { RightContext(it) },
            maxResults?.let { MaxResults(it) },
            metadata?.let { Metadata(it) }
        )
    }
}

/**
 * Builds the URL address for the Datamuse API. In order to be able to build a URL you need to
 * provide at least a hard constraint.
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
 * @throws IllegalHardConstraintState - when no hard constraint is specified.
 * Reason: An endpoint with no hard constraint will yield no result.
 * */
fun buildWordsEndpointUrl(wordsConfig: WordsEndpointBuilder.() -> Unit):
        WordsEndpointBuilder {
    val builder = WordsEndpointBuilder()
    builder.wordsConfig()
    if (builder.hardConstraints.isEmpty())
        throw IllegalHardConstraintState(
            "The hard constraint set is empty. You need to provide at least one hard constraint in order to build a URL for the API."
        )
    return builder
}