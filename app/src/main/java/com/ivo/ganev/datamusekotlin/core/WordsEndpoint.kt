package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.core.WordsEndpoint.*
import java.util.*
import java.util.EnumSet.of

data class WordEndpointUrlConfig(
    @JvmField val hardConstraint: HardConstraint,
    @JvmField val topic: Topic,
    @JvmField val leftContext: LeftContext,
    @JvmField val rightContext: RightContext,
    @JvmField val maxResults: MaxResults,
    @JvmField val metadata: WordsEndpoint.Metadata
)

class WordsEndpoint {
    interface EndpointKey {
        val key: String
        val value: String
    }

    abstract class HardConstraint(open val constraint: String) : EndpointKey {
        class MeansLike(constraint: String) : HardConstraint(constraint) {
            override val key: String get() = "ml"
        }

        class SoundsLike(constraint: String) : HardConstraint(constraint) {
            override val key: String get() = "sl"
        }

        class SpelledLike(constraint: String) : HardConstraint(constraint) {
            override val key: String get() = "sp"
        }

        class RelatedWords(private val code: Code, constraint: String) :
            HardConstraint(constraint) {

            enum class Code(val value: String) {
                POPULAR_NOUNS("jja"),
                POPULAR_ADJECTIVES("jjb"),
                SYNONYMS("syn"),
                TRIGGERS("trg"),
                ANTONYMS("ant"),
                KIND_OF("spc"),
                MORE_GENERAL_THAN("gen"),
                COMPRISES("com"),
                PART_OF("par"),
                FREQUENT_FOLLOWERS("bga"),
                FREQUENT_PREDECESSORS("bgb"),
                RHYMES("rhy"),
                APPROXIMATE_RHYMES("nry"),
                HOMOPHONES("hom"),
                CONSONANT_MATCH("cns")
            }

            override val key: String
                get() = "rel_${code.value}"
        }

        override val value: String
            get() = constraint
    }

    class Topic(private vararg val topics: String) : EndpointKey {
        init {
            if (topics.size > 5)
                throw IllegalArgumentException("Maximum number of topics is 5")
        }

        override val key: String get() = "topics"
        override val value: String
            get() = buildString {
                topics.forEach { append(it) }
            }
    }

    data class LeftContext(val leftContext: String) : EndpointKey {
        override val key: String get() = "lc"
        override val value: String get() = leftContext
    }

    data class RightContext(val rightContext: String) : EndpointKey {
        override val key: String get() = "rc"
        override val value: String get() = rightContext
    }

    data class MaxResults(val max: Int) : EndpointKey {
        override val key: String get() = "max"
        override val value: String get() = max.toString()
    }

    data class Metadata(val flags:  EnumSet<Flag>) : EndpointKey {
        enum class Flag(val identifier: String) {
            DEFINITIONS("d"),
            PARTS_OF_SPEECH("p"),
            SYLLABLE_COUNT("s"),
            PRONUNCIATIONS("r"),
            WORD_FREQUENCY("f");

            infix fun and(other: Flag): EnumSet<Flag> = of(this, other)
        }

        override val key: String get() = "md"
        override val value: String get() = buildString { for (flag in flags) { append(flag.identifier)} }
    }
}

infix fun EnumSet<Metadata.Flag>.and(other: Metadata.Flag): EnumSet<Metadata.Flag> = of(other, *this.toTypedArray())
