package com.ivo.ganev.datamusekotlin.internal

import androidx.annotation.Keep
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

@Keep
@Serializable
internal data class Words(val wordRequest: List<WordRequest>)

@Keep
@Serializable(with = WordAsObjectSerializer::class)
internal data class WordRequest(
    val word: String = "",
    val score: Int = -1,
    val definitions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val numSyllables: Int = -1
)

internal object WordAsObjectSerializer : KSerializer<WordRequest> {
    // This describes the JSON file. The element names should match exactly the JSON keys
    // otherwise you'll get empty/non-initialized fields in the constructed class
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WordRequest") {
        element<String>("word")
        element<Int>("score")
        element<List<String>>("defs")
        element<List<String>>("tags")
        element<Int>("numSyllables")
    }

    class WordRequestBuilder internal constructor(wordRequest: WordRequest) {
        var word = wordRequest.word
        var score = wordRequest.score
        var definitions = wordRequest.definitions
        var tags = wordRequest.tags
        var numSyllables = wordRequest.numSyllables

        fun build(): WordRequest {
            return WordRequest(word, score, definitions, tags, numSyllables)
        }
    }

    override fun deserialize(decoder: Decoder): WordRequest = decoder.decodeStructure(descriptor) {
        val wordRequestBuilder = WordRequestBuilder(WordRequest())
        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> wordRequestBuilder.word = decodeStringElement(descriptor, 0)
                1 -> wordRequestBuilder.score = decodeIntElement(descriptor, 1)
                2 -> wordRequestBuilder.definitions = decodeSerializableElement(
                    descriptor,
                    2,
                    ListSerializer(String.serializer()),
                    null
                )
                3 -> wordRequestBuilder.tags = decodeSerializableElement(
                    descriptor,
                    3,
                    ListSerializer(String.serializer()),
                    null
                )
                4 -> wordRequestBuilder.numSyllables = decodeIntElement(descriptor, 4)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected Index: $index")
            }
        }
        wordRequestBuilder.build()
    }

    override fun serialize(encoder: Encoder, value: WordRequest) = encoder.encodeStructure(
        descriptor
    ) {
        encodeStringElement(descriptor, 0, value.word)
        encodeIntElement(descriptor, 1, value.score)
    }
}