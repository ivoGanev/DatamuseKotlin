package com.ivo.ganev.datamusekotlin.core

import android.net.Uri
import com.ivo.ganev.datamusekotlin.api.Configuration

internal class UrlProducer(endpointKeyValues: List<EndpointKeyValue?>) {
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
