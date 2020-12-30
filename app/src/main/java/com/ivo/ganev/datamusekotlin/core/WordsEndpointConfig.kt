package com.ivo.ganev.datamusekotlin.core

import android.net.Uri
import com.ivo.ganev.datamusekotlin.api.*
import java.util.*

internal data class WordsEndpointConfig(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
)

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