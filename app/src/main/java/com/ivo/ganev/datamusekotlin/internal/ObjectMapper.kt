package com.ivo.ganev.datamusekotlin.internal

import com.ivo.ganev.datamusekotlin.api.DatamuseOkHttpClientGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class ObjectMapper {
    fun mapWords(jsonString: String) : Words {

            val decodeFromJsonElement = Json {
                ignoreUnknownKeys = true
            } .decodeFromString<WordRequest>(jsonString)
            println(decodeFromJsonElement)

        // When fetching a result it always returns an array of words
        // - Always contains "score" and "word"
        // - Optionally contains tags
        // - Tags may or may not be provided
        // - If tags are provided not all of them are always returned

        // One naive idea is to provide multiple versions of KSerializer so that it covers all
        // possibilities for the incomming json object

        return Words(emptyList())
    }
}