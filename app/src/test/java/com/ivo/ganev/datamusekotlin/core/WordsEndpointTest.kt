package com.ivo.ganev.datamusekotlin.core

import org.junit.Test

class WordsEndpointTest {
    @Test
    fun `test if url comes out correct`() {
        wordsEndpoint {
            TODO("Complete the function to produce the endpoint url address: https://api.datamuse../words?hc=\"sweet\"..)
            hardConstraint = "sweet"
            maxResults = 100
        }
    }
}