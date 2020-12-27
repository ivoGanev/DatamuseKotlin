package com.ivo.ganev.datamusekotlin.core


import com.ivo.ganev.datamusekotlin.core.HardConstraint.*
import org.junit.Test

class WordsEndpointTest {
    @Test
    fun `test if url comes out correct`() {
        val b = wordsEndpointUrl {
      //      word = "hello"
            hardConstraint = MeansLike("as")
            topic = "meh"
            leftContext = "left"
            rightContext = "right"
            metadata = MetadataFlag.PARTS_OF_SPEECH and MetadataFlag.DEFINITIONS and MetadataFlag.PRONUNCIATIONS
            maxResults = 100
        }

        println(b)
    }
}