package com.ivo.ganev.datamusekotlin.internal

import kotlinx.serialization.Serializable

@Serializable
data class WordRequest(
    val word: String,
    val score: Int,
    val metadata: WordRequestMetadata? = null
)

@Serializable
data class WordRequestMetadata(
    val definitions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val syllableCount: Int = 0
)