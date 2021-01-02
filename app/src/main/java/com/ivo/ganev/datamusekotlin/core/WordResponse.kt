package com.ivo.ganev.datamusekotlin.core

import androidx.annotation.Keep
import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.*
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
     *
     * Let's say that "response" is a retrieved [WordResponse] variable
     * and we want to get the score from the second array element which would be "3".
     * We can do this with the index access operator like: response.elementAt(1)[Score::class],
     * which will give us the single [Element] object in our case [Score] with value of "3".
     *
     * @returns [Element] if the set contains it and null when there is
     * no element of the provided type.
     * */
    inline operator fun <reified T : Element> get(element: KClass<T>): T? {
        return elements.find { element.isInstance(it) && element != Element::class } as T?
    }
}