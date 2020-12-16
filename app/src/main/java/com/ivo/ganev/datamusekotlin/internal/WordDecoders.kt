package com.ivo.ganev.datamusekotlin.internal

import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json


internal interface DatamuseJsonResponseDecoder {
    fun decode(jsonString: String): Set<DatamuseJsonWordResponse>
}

internal class KotlinJsonWordDecoder : DatamuseJsonResponseDecoder {
    override fun decode(jsonString: String): Set<DatamuseJsonWordResponse> =
        Json { ignoreUnknownKeys = true }.decodeFromString(
            SetSerializer(DatamuseJsonWordResponse.serializer()), jsonString
        )
}