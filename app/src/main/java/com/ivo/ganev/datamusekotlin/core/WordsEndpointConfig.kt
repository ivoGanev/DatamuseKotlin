package com.ivo.ganev.datamusekotlin.core

import android.net.Uri
import java.util.*

internal data class WordsEndpointConfig(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
)

class WordsEndpointBuilder {
    /**
     * See [HardConstraint]
     * */
    var hardConstraint: HardConstraint = HardConstraint.MeansLike("")

    /**
     * See: [Topic]
     * */
    var topics: String? = null

    /**
     * See: [LeftContext]
     * */
    var leftContext: String? = null

    /**
     * See: [RightContext]
     * */
    var rightContext: String? = null

    /**
     * See: [MaxResults]
     * */
    var maxResults: Int? = null

    /**
     * See: [Metadata]
     * */
    var metadata: EnumSet<MetadataFlag>? = null

    internal fun build(): WordsEndpointConfig {
        return WordsEndpointConfig(
            hardConstraint,
            topics?.let { Topic(it) },
            leftContext?.let { LeftContext(it) },
            rightContext?.let { RightContext(it) },
            maxResults?.let { MaxResults(it) },
            metadata?.let { Metadata(it) }
        )
    }
}

internal class WordsEndpointsUrlBuilder(endpointKeyValues: List<EndpointKeyValue?>) {
    companion object {
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