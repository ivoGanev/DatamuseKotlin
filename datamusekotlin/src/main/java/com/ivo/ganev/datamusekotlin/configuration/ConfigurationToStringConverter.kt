package com.ivo.ganev.datamusekotlin.configuration

import com.ivo.ganev.datamusekotlin.endpoint.EndpointKeyValue
import com.ivo.ganev.datamusekotlin.core.UrlBuilder

/**
 * Converts the [Configuration] into any type of String.
 * Currently the library supports the configuration to
 * be converted only to URL in the form of a String which
 * would be quite sufficient for most use cases.
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