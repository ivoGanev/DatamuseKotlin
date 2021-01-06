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
package com.ivo.ganev.datamusekotlin.endpoint.words

import androidx.annotation.Keep
import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse.Element.*
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Keep
@Serializable(with = WordResponseSerializer::class)
class WordResponse(val elements: Set<Element>) {
    @Serializable
    sealed class Element {
        @Serializable
        data class Word(val word: String) : Element()

        @Serializable
        data class Score(val score: Int) : Element()

        @Serializable
        data class Definitions(val defs: List<String>) : Element()

        @Serializable
        data class Tags(val tags: List<String>) : Element()

        @Serializable
        data class SyllablesCount(val numSyllables: Int = -1) : Element()

        @Serializable
        data class DefHeadwords(val defHeadword: String = "") : Element()
    }

    /**
     * *  Retrieves a single element from the element set: [elements]
     * @param element is a KClass of type [Element] like [Score]. Example "Score::class"
     *
     * ```
     * Example: Let's look at the JSON response:
     * [
     *  {
     *      "word": "hippopotamus",
     *      "score": 501
     *  },
     *  {
     *      "word": "hippotamus",
     *      "score": 3
     *  }
     * ]
     *```
     *
     * In order to retrieve the [Word] element all you need to do is call:
     *
     * myWordResponse[Word::class]?.word
     *
     * Elements are nullable for the reason that not every json response
     * will carry all of them.
     *
     * @returns [Element] if the set contains it and null when there is
     * no element of the provided type.
     * */
    inline operator fun <reified T : Element> get(element: KClass<T>): T? {
        return elements.find { element.isInstance(it) && element != Element::class } as T?
    }

    /**
     * Same as [get] but with different syntax
     *
     * Example:
     *
     * val word = wordResponse.get<Word>()?.word
     * */
    inline fun <reified T : Element> get() : T? {
        for (element in elements)
            if(element is T) return element
        return null
    }
}