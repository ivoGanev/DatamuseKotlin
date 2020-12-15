package com.ivo.ganev.datamusekotlin

import androidx.test.platform.app.InstrumentationRegistry
import com.ivo.ganev.datamusekotlin.internal.KotlinJsonWordDecoder
import kotlinx.serialization.InternalSerializationApi
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class DatamuseKotlinWordRequestMapperTest {
    companion object RequestFile {
        const val DEFINITIONS = "word-request-metadata-definitions.json"
        const val SIMPLE = "word-request-simple.json"
        const val SYLLABLES = "word-request-syllables.json"
        const val TAGS = "word-request-tags.json"
    }
    
    @Test
    fun testDefinitions() {
        val file = read(DEFINITIONS)
        val mapper = KotlinJsonWordDecoder()
        val mapWords = mapper.map(file)
        mapWords.forEach {
            println(it.elements)
        }
    }

    private fun read(path: String): String {
        val assets = InstrumentationRegistry.getInstrumentation().targetContext.assets
        val file = assets.open(path)
        val reader = BufferedReader(InputStreamReader(file))
        reader.use {
            return it.readText()
        }
    }
}