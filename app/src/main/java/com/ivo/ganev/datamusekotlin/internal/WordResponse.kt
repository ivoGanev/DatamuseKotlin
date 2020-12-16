package com.ivo.ganev.datamusekotlin.internal

import androidx.annotation.Keep
import com.ivo.ganev.datamusekotlin.internal.WordResponse.Element.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlin.reflect.KClass

@Keep
@Serializable(with = WordResponseSerializer::class)
internal class WordResponse(private val elements: Set<Element>) {

    val responseSize get() = elements.size

    @Serializable
    internal sealed class Element {
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

internal object WordResponseSerializer : KSerializer<WordResponse> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WordResponse") {
        // Each of these elements corresponds to the json response from Datamuse. Their name must
        // match exactly the response key or it won't be considered for deserialization.
        element<String>("word")
        element<Int>("score")
        element<List<String>>("defs")
        element<List<String>>("tags")
        element<Int>("numSyllables")
        element<String>("defHeadword")
    }

    override fun deserialize(decoder: Decoder): WordResponse =
        decoder.decodeStructure(descriptor) {
            var word: Word? = null
            var score: Score? = null
            var definitions: Definitions? = null
            var tags: Tags? = null
            var numSyllables: SyllablesCount? = null
            var defHeadword: DefHeadwords? = null

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> word = Word(decodeStringElement(descriptor, 0))
                    1 -> score = Score(decodeIntElement(descriptor, 1))
                    2 -> definitions = Definitions(
                        decodeSerializableElement(
                            descriptor,
                            2,
                            ListSerializer(String.serializer())
                        )
                    )
                    3 -> tags = Tags(
                        decodeSerializableElement(
                            descriptor,
                            3,
                            ListSerializer(String.serializer())
                        )
                    )
                    4 -> numSyllables = SyllablesCount(decodeIntElement(descriptor, 4))
                    5 -> defHeadword = DefHeadwords(decodeStringElement(descriptor, 5))
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            val list = setOfNotNull(word, score, definitions, tags, numSyllables, defHeadword)
            WordResponse(list)
        }


    override fun serialize(encoder: Encoder, value: WordResponse) {
        error("Serialization is not supported.")
    }
}