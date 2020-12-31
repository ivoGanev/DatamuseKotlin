package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import java.util.*


class WordsEndpointBuilder {
    /**
     * @see [HardConstraint]
     * */
    var hardConstraint: HardConstraint = HardConstraint.MeansLike("")

    /**
     * @see [Topic]
     * */
    var topics: String? = null

    /**
     * @see  [LeftContext]
     * */
    var leftContext: String? = null

    /**
     * @see  [RightContext]
     * */
    var rightContext: String? = null

    /**
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
