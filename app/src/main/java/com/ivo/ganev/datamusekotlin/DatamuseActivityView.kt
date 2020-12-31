package com.ivo.ganev.datamusekotlin

import androidx.lifecycle.LiveData
import com.ivo.ganev.datamusekotlin.UiConstraintElement.RelatedWordsElement
import com.ivo.ganev.datamusekotlin.api.*
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code.APPROXIMATE_RHYMES
import com.ivo.ganev.datamusekotlin.core.WordResponse
import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import java.util.*

abstract class DatamuseActivityData()  {
    private val client = DatamuseClient()

    abstract fun getConstraint(): HardConstraint

    protected open fun getTopics(): String? {
        return null
    }

    protected open fun getLeftContext(): String? {
        return null
    }

    protected open fun getRightContext(): String? {
        return null
    }

    protected open fun getMaxResults(): Int? {
        return null
    }

    protected open fun getMetadata(): EnumSet<MetadataFlag>? {
        return null
    }

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

