package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import com.ivo.ganev.datamusekotlin.core.WordsEndpointsUrlBuilder
import com.ivo.ganev.datamusekotlin.core.toWordEndpointKeyValue
import java.util.*



interface ConfiguredUrlString {
    /**
     * Will create a Url string from [WordsEndpointConfigBuilder] configuration
     * */
    fun from(config: WordsEndpointConfigBuilder) : String
}

/**
 * This is a builder designed specifically for library clients.
 * */
class WordsEndpointConfigBuilder {
    object UrlString: ConfiguredUrlString {
       override fun from(config: WordsEndpointConfigBuilder) : String {
            val endpointKeyValue = toWordEndpointKeyValue(config.build())
            return WordsEndpointsUrlBuilder(endpointKeyValue).build()
        }
    }
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
