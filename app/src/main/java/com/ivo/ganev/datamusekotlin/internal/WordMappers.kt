package com.ivo.ganev.datamusekotlin.internal

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement

internal interface WordMapper {
    fun map(jsonString: String): List<WordRequest>
}

internal class DatamuseKotlinWordMapper : WordMapper {
    override fun map(jsonString: String): List<WordRequest> =
        Json {
            ignoreUnknownKeys = true
        }.decodeFromString(ListSerializer(WordRequest.serializer()), jsonString)
}