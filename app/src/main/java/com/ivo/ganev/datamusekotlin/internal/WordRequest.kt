package com.ivo.ganev.datamusekotlin.internal

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Keep
@Serializable
internal data class WordRequest(
    val word: String = "",
    val score: Int = -1,
    val definitions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val numSyllables: Int = -1,
    val defHeadword: String = ""
)
