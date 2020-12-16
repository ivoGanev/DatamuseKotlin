package com.ivo.ganev.datamusekotlin.core

import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json


internal interface DatamuseJsonResponseDecoder {
    fun decode(jsonString: String): Set<WordResponse>
}

internal class KotlinJsonWordDecoder : DatamuseJsonResponseDecoder {

    /**
     * Decodes a Json body to a set of word responses
     * @throws [SerializationException] if the given JSON string cannot be deserialized
     * */
    override fun decode(jsonString: String): Set<WordResponse> =
        Json { ignoreUnknownKeys = true }.decodeFromString(
            SetSerializer(WordResponse.serializer()), jsonString
        )
}