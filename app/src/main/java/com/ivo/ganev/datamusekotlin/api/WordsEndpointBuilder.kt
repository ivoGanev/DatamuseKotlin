package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import java.util.*


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
