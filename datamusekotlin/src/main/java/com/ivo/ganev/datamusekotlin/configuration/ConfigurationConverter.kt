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

import com.ivo.ganev.datamusekotlin.endpoint.EndpointKeyValue
import com.ivo.ganev.datamusekotlin.endpoint.DatamuseUrlBuilder

/**
 * Converts an endpoint configuration into into a string.
 * */
class ConfigurationConverter {
    companion object {
        private const val WORDS_ENDPOINT_PATH = "words"

        /**
         * It will convert all the elements of the [WordsEndpointConfig], e.g.
         * HardConstraint("elephant"), Topics("sea") and so on, into a
         * Datamuse URL address in the form of a String like:
         * ```
         *
         * https://api.datamuse.com/words?ml=elephant&topics=sea
         * ```
         * @return the Datamuse Url as a String
         * */
        fun toWordsEndpoint(config: WordsEndpointConfig): String {
            val endpointKeyValue = toWordEndpointKeyValue(config)
            return DatamuseUrlBuilder(endpointKeyValue, WORDS_ENDPOINT_PATH).build()
        }

        /**
         * Creates a list of [EndpointKeyValue] from [WordsEndpointConfig]
         * */
        private fun toWordEndpointKeyValue(buildConfig: WordsEndpointConfig):
                List<EndpointKeyValue?> = with(buildConfig) {
            listOf(hardConstraint, topic, leftContext, rightContext, maxResults, metadata)
        }
    }


}