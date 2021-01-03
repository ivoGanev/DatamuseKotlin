package com.ivo.ganev.datamusekotlin.api

import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.Definitions
import com.ivo.ganev.datamusekotlin.core.filterFirst
import java.lang.StringBuilder
import java.util.regex.Pattern


private const val nounPattern = "^n[\t]"
private const val adjectivePattern = "^adj[\t]"
private const val adverbPattern = "^adv[\t]"
private const val verbPattern = "^v[\t]"

private val regexMap = mapOf(
    SpeechPart.NOUN to nounPattern,
    SpeechPart.ADJECTIVE to adjectivePattern,
    SpeechPart.ADVERB to adverbPattern,
    SpeechPart.VERB to verbPattern
)

enum class SpeechPart(val label: String) {
    NOUN("Noun"),
    ADJECTIVE("Adjective"),
    ADVERB("Adverb"),
    VERB("Verb")
}

fun List<Pair<SpeechPart?, String>>.buildToString() : String  {
    val result = StringBuilder()
    for (element in this) {
        val first = element.first?.label ?: ""
        result.append("($first), ${element.second}\n")
    }
    return result.toString()
}

fun Definitions.format() : List<Pair<SpeechPart?, String>> {
    val result = mutableListOf<Pair<SpeechPart?, String>>()

    for (definition in defs) {
        var definitionCopy = definition
        val speechRegex = regexMap.filterFirst {
            val pattern = Pattern.compile(it)
            val matcher = pattern.matcher(definition)
            val matchResult = matcher.find()
            matchResult
        }

        if (speechRegex != null)
            definitionCopy = definitionCopy.replace(speechRegex.second.toRegex(), "")
        result.add(Pair(speechRegex?.first, definitionCopy))
    }
    return result
}

