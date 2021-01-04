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
import com.ivo.ganev.datamusekotlin.endpoint.UrlBuilder

/**
 * Converts the [Configuration] into a string.
 * */
abstract class ConfigurationToStringConverter {
    /**
     * Converts [Configuration] to URL in the form of String
     * */
    abstract fun from(config: Configuration): String

    /**
     * The [Default] converter is quite sufficient in most use cases.
     * It will convert all the elements of the configuration, e.g.
     * HardConstraint("elephant"), Topics("sea") and so on, into a
     * Datamuse URL address in the form of a String including the
     * authority, path and query parameters like:
     * https://api.datamuse.com/words?ml=elephant&topics=sea
     * */
    object Default : ConfigurationToStringConverter() {
        override fun from(config: Configuration): String {
            val endpointKeyValue = toWordEndpointKeyValue(config)
            return UrlBuilder(endpointKeyValue).build()
        }

        /**
         * Creates a list of [EndpointKeyValue] from [Configuration]
         * */
        private fun toWordEndpointKeyValue(buildConfig: Configuration):
                List<EndpointKeyValue?> = with(buildConfig) {
            listOf(hardConstraint, topic, leftContext, rightContext, maxResults, metadata)
        }
    }
}