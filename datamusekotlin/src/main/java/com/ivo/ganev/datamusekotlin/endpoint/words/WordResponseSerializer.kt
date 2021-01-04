package com.ivo.ganev.datamusekotlin.endpoint.words

import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse.Element.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

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