/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivo.ganev.datamuse_kotlin.common

import com.ivo.ganev.datamuse_kotlin.endpoint.words.WordResponse
import com.ivo.ganev.datamuse_kotlin.endpoint.words.WordResponse.Element.Definitions
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

/**
 * A method to display the Pair like:
 *
 * (Noun), "whatever the definition of the word is"
 *
 * (Verb), "second definition of the word"
 *
 * */
fun List<Pair<SpeechPart?, String>>.string() : String  {
    val result = StringBuilder()
    for (element in this) {
        val first = element.first?.label ?: ""
        result.append("($first), ${element.second}\n")
    }
    return result.toString()
}

/**
 * This method extracts the part of speech
 * as [SpeechPart] and removes the unnecessary noise from the definition leaving it
 * looking like:
 *```
 *  "coagulated milk; used to made cheese",
 *  "coagulated liquid resembling milk curd"
 *  ```
 *
 * Why format?
 *
 * When querying for definitions they come out in a quite unhandy format looking like:
 * ```
 * {
 * "word": "curds",
 * "defs": [
 *      "n\tcoagulated milk; used to made cheese",
 *      "n\ta coagulated liquid resembling milk curd"
 * ],}
 * ```
 * Notice the "n\t.." in the beginning of the definition indicates the part of speech for the word
 * in this case a noun for both definitions. In case you would like to eliminate that, you could use
 * this method.
 * */
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

