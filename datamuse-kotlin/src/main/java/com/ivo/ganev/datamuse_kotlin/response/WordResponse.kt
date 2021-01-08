/**
 * Copyright (C) 2020 Ivo Ganev
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
package com.ivo.ganev.datamuse_kotlin.response

import com.ivo.ganev.datamuse_kotlin.endpoint.internal.WordResponseSerializer
import com.ivo.ganev.datamuse_kotlin.response.WordResponse.Element
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable(with = WordResponseSerializer::class)

/**
 * The WordResponse is representing a single word coming from the JSON response
 * with its properties which are defined as [Element]. So for example,
 * ```
 *   {
 *    "word": "shruff",
 *    "score": 93,
 *    "numSyllables": 1
 *   }
 *   ```
 *   could be retrieved like:
 *   ```
 * wordResponses.forEach {
 * for (element in it.elements) {
 *      when (element) {
 *           is WordResponse.Element.Word -> queryTextView.append("\nWord: ${element.word}")
 *            is WordResponse.Element.Score ->  queryTextView.append("\n Score: ${element.score}")
 *            // you can use format() to separate the part of word from the definition because the response comes as:
 *            // "defs":["adj\tof great mass; huge and bulky"]}
 *            is WordResponse.Element.Definitions ->  queryTextView.append("\n Definitions: ${element.format().string()}")
 *            is WordResponse.Element.Tags ->  queryTextView.append("\n Tags: ${element.tags}")
 *            is WordResponse.Element.SyllablesCount ->  queryTextView.append("\n Syllable Count: ${element.numSyllables}")
 *            is WordResponse.Element.DefHeadwords ->  queryTextView.append("\n DefHeadwords ${element.defHeadword}")
 *        }
 *    }
 *}
 ```
*/
class WordResponse(val elements: Set<Element>)  {
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
     * @param element is a KClass of type [Element] like [Element.Score]. Example "Score::class"
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
     * In order to retrieve the [Element.Word] element all you need to do is call:
     *
     * myWordResponse[Word::class]?.word
     *
     * @returns [Element] if the set contains it and null when there is
     * no element of the provided type.
     * */
    inline operator fun <reified T : Element> get(element: KClass<T>): T? {
        return elements.find { element.isInstance(it) && element != Element::class } as T?
    }
}