package com.ivo.ganev.datamusekotlin

import androidx.test.platform.app.InstrumentationRegistry
import com.ivo.ganev.datamusekotlin.internal.DatamuseJsonWordResponse.Element.*
import com.ivo.ganev.datamusekotlin.internal.KotlinJsonWordDecoder
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
        val file = read(SIMPLE)
        val mapper = KotlinJsonWordDecoder()
        val mapWords = mapper.decode(file)
        mapWords.forEach {
            //println(it.elements)
        }
        val elementAt = mapWords.elementAt(0)
        val list = elementAt[Score::class]

        println(list)
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