package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.endpoint.EndpointKeyValue
import okhttp3.HttpUrl

/**
 * Builds a URL String from the list of [EndpointKeyValue]
 * */
internal class UrlBuilder(endpointKeyValues: List<EndpointKeyValue?>) {
    internal companion object {
        const val SCHEME = "https"
        const val AUTHORITY = "api.datamuse.com"
        const val PATH = "words"
    }

    private val query: List<EndpointKeyValue> = endpointKeyValues.filterNotNull()

    fun build(): String = HttpUrl.Builder().apply {
        scheme(SCHEME)
        host(AUTHORITY)
        addPathSegment(PATH)
        for (element in query) addQueryParameter(element.key, element.value)
    }.build().toString()
}
