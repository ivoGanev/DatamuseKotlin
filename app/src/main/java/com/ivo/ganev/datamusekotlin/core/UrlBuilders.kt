package com.ivo.ganev.datamusekotlin.core

import android.net.Uri

internal class WordsEndpointsUrlBuilder(endpointKeyValues: List<EndpointKeyValue?>) {
    internal companion object {
        const val SCHEME = "https"
        const val AUTHORITY = "api.datamuse.com"
        const val PATH = "words"
    }

    private val query: List<EndpointKeyValue> = endpointKeyValues.filterNotNull()

    fun build(): String = Uri.Builder().apply {
        scheme(SCHEME)
        authority(AUTHORITY)
        path(PATH)
        for (element in query) appendQueryParameter(element.key, element.value)
    }.toString()
}

/**
 * Creates a list of [EndpointKeyValue] from [WordsEndpointConfig]
 *
 * absolutely necessary to create a URL for the API query.
 * */
internal fun toWordEndpointKeyValue(buildConfig: WordsEndpointConfig):
        List<EndpointKeyValue?> = with(buildConfig) {
    listOf(hardConstraint, topic, leftContext, rightContext, maxResults, metadata)
}

