package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.api.*
import java.util.*

/**
 * You can use this class as a medium between your input data from the application views
 * and the generation of the URL address for Datamuse. By inheriting from it you can directly
 * bind the data from the UI to the URL builder. See [DemoModelWordsEndpointsUrlBinder] for a concrete
 * implementation example.
 * */
abstract class ModelWordsEndpointsUrlBinder() {

    /**
     * All Datamuse /words endpoint queries require a single hard constraint
     * to return a meaningful result
     * */
    abstract fun getConstraint(): HardConstraint

    /**
     * Override to build query with topics
     * */
    protected open fun getTopics(): String = ""

    /**
     * Override to build query with left context
     * */
    protected open fun getLeftContext(): String = ""

    /**
     * Override to build the query with right context
     * */
    protected open fun getRightContext(): String = ""

    /**
     * Override to set the query with max results. Default is 100
     * */
    protected open fun getMaxResults(): Int = 100

    /**
     * Override to build the query with metadata flags
     * */
    protected open fun getMetadata(): EnumSet<MetadataFlag> = EnumSet.noneOf(MetadataFlag::class.java)

    private fun getRightContextNullable() : String? {
        return if (getRightContext() == "") null
        else getRightContext()
    }

    private fun getLeftContextNullable() : String? {
        return if (getLeftContext() == "") null
        else getLeftContext()
    }
    private fun getTopicsNullable(): String? {
        return if (getTopics() == "") null
        else getTopics()
    }

    private fun getMaxResultsNullable(): Int? {
        return if (getMaxResults() < 0 || getMaxResults() > 1000) null
        else getMaxResults()
    }

    private fun getMetadataNullable() : EnumSet<MetadataFlag>? {
        return if (getMetadata().isEmpty()) null
        else getMetadata()
    }

    /**
     * Builds a URL from the "get" methods. The Datamuse library uses nullable types
     * for it's query elements to know whether or not it should add the query to the
     * url building process. For example if we build the Url with HardConstraint.MeansLike("donkey")
     * the Url address will be https://api.datamuse.com/words?ml=donkey. If we decide to add topics
     * to the url instead of null we have to create a new Topics("topic") and the address will become
     * https://api.datamuse.com/words?ml=donkey&topics="topic"
     * */
    fun toUrlString(): String {
        return buildWordsEndpointUrl {
            hardConstraint = getConstraint()
            topics = getTopicsNullable()
            leftContext = getLeftContextNullable()
            rightContext = getRightContextNullable()
            maxResults = getMaxResultsNullable()
            metadata = getMetadataNullable()
        }
    }
}

