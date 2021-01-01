package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import java.util.*


/**
 * Builds a URL from the "get" methods. The Datamuse library uses nullable types
 * for it's query elements to know whether or not it should add the query to the
 * url building process. For example if we build the Url with HardConstraint.MeansLike("donkey")
 * the Url address will be https://api.datamuse.com/words?ml=donkey. If we decide to add topics
 * to the url instead of null we have to create a new Topics("topic") and the address will become
 * https://api.datamuse.com/words?ml=donkey&topics="topic"
 * */
class WordsEndpointBuilder {
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
