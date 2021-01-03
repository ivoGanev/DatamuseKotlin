package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import com.ivo.ganev.datamusekotlin.core.WordsEndpointsUrlBuilder
import com.ivo.ganev.datamusekotlin.core.toWordEndpointKeyValue
import java.util.*

/**
 * Returns the full address made of [WordsEndpointConfigBuilder]
 * */
fun toWordsEndpointUrl(config: WordsEndpointConfigBuilder) : String {
    val endpointKeyValue = toWordEndpointKeyValue(config.build())
    return WordsEndpointsUrlBuilder(endpointKeyValue).build()
}

class WordsEndpointConfigBuilder {
    /**
     * A query element for the hard constraints.
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
