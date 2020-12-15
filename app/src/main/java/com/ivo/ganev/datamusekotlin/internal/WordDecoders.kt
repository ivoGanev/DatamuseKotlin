package com.ivo.ganev.datamusekotlin.internal

import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json


internal interface DatamuseJsonResponseDecoder {
    fun map(jsonString: String): Set<DatamuseJsonResponse>
}

internal class KotlinJsonWordDecoder : DatamuseJsonResponseDecoder {
    override fun map(jsonString: String): Set<DatamuseJsonResponse> =
        Json { ignoreUnknownKeys = true }.decodeFromString(
            SetSerializer(DatamuseJsonResponse.serializer()), jsonString
        )
}