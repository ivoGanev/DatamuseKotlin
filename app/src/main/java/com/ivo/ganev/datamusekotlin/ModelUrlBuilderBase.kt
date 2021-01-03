package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.api.*
import java.util.*

/**
 * You can use this class as a medium between your input data from the application views
 * and the generation of the URL address for Datamuse. By inheriting from it you can directly
 * bind the data from the UI to the URL builder. See [ModelUrlBuilder]
 * for a concrete implementation example.
 * */
abstract class ModelUrlBuilderBase() {

    /**
     * All Datamuse /words endpoint queries require a single hard constraint
     * to return a meaningful result
     * */
    abstract fun constraint(): HardConstraint

    /**
     * Override to build query with topics
     * */
    protected open fun topics(): String = ""

    /**
     * Override to build query with left context
     * */
    protected open fun leftContext(): String = ""

    /**
     * Override to build the query with right context
     * */
    protected open fun rightContext(): String = ""

    /**
     * Override to set the query with max results. Default is 100
     * */
    protected open fun maxResults(): Int = 100

    /**
     * Override to build the query with metadata flags
     * */
    protected open fun metadata(): EnumSet<MetadataFlag> = EnumSet.noneOf(MetadataFlag::class.java)

    private fun topicsNullable(): String? {
        return if (topics() == "") null
        else topics()
    }

    private fun rightContextNullable() : String? {
        return if (rightContext() == "") null
        else rightContext()
    }
    private fun leftContextNullable() : String? {
        return if (leftContext() == "") null
        else leftContext()
    }

    private fun maxResultsNullable(): Int? {
        return if (maxResults() < 0 || maxResults() > 1000) null
        else maxResults()
    }

    private fun metadataNullable() : EnumSet<MetadataFlag>? {
        return if (metadata().isEmpty()) null
        else metadata()
    }

    fun build(): WordsEndpointConfigBuilder {
        return buildWordsEndpointUrl {
            hardConstraint = constraint()
            topics = topicsNullable()
            leftContext = leftContextNullable()
            rightContext = rightContextNullable()
            maxResults = maxResultsNullable()
            metadata = metadataNullable()
        }
    }
}

