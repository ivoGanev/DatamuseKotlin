package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.core.WordsEndpoint.HardConstraint.MeansLike
import org.junit.Test

class WordsEndpointTest {
    @Test
    fun `test if url comes out correct`() {
        val b = wordsEndpoint {
            word = "hello"
            hardConstraint = MeansLike
            topic = "meh"
            leftContext = "left"
            rightContext = "right"
            metadata = MetadataFlag.PARTS_OF_SPEECH and MetadataFlag.DEFINITIONS and MetadataFlag.PRONUNCIATIONS
            maxResults = 100
        }

        println(b)
    }
}