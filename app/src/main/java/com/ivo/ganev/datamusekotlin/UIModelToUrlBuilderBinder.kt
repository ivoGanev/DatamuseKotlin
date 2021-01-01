package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.api.*
import java.util.*

/**
 * You can use this class as a medium between your input data from the application views
 * and the generation of the URL address for Datamuse. By inheriting from it you can directly
 * bind the data from the UI to the URL builder. See [DemoModelToUrlBuilderBuilderBinder] for a concrete
 * implementation example.
 * */
abstract class UIModelToUrlBuilderBinder()  {

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
    protected open fun getMetadata(): EnumSet<MetadataFlag>?  = null

    /**
     * Builds a URL from the bind UI data
     * */
    fun toUrlString(): String {
        return buildWordsEndpointUrl {
            hardConstraint = getConstraint()
            topics = getTopics()
            leftContext = getLeftContext()
            rightContext = getRightContext()
            maxResults = getMaxResults()
            metadata = getMetadata()
        }
    }
}

