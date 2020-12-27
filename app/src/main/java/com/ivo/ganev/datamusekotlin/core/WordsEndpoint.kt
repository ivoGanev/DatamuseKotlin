package com.ivo.ganev.datamusekotlin.core


import java.util.*
import java.util.EnumSet.of

typealias MetadataFlag = Metadata.Flag

data class WordsEndpointConfig(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
)

class WordsEndpointBuilder {
    var hardConstraint: HardConstraint = HardConstraint.MeansLike("")
    var topic: String? = null
    var leftContext: String? = null
    var rightContext: String? = null
    var maxResults: Int? = null
    var metadata: EnumSet<MetadataFlag>? = null

    fun build(): WordsEndpointConfig {
        return WordsEndpointConfig(
            hardConstraint,
            topic?.let { Topic(it) },
            leftContext?.let { LeftContext(it) },
            rightContext?.let { RightContext(it) },
            maxResults?.let { MaxResults(it) },
            metadata?.let { Metadata(it) }
        )
    }
}

internal class KeyValueEndpointsUrlStringBuilder(endpointKeyValues: List<EndpointKeyValue?>) {
    private val address: String = "https://api.datamuse.com/words?"
    private val path: List<EndpointKeyValue> = endpointKeyValues.filterNotNull()
    fun build(): String {
        return address + path.joinToString(separator = "&") { "${it.key}=${it.value}" }
    }
}

fun wordsEndpointUrl(endpointConfig: WordsEndpointBuilder.() -> Unit): String {
    val builder = WordsEndpointBuilder()
    builder.endpointConfig()
    val buildConfig = builder.build()
    val path = with(buildConfig) {
        listOf(hardConstraint, topic, leftContext, rightContext, maxResults, metadata)
    }
    return KeyValueEndpointsUrlStringBuilder(path).build()
}

interface EndpointKeyValue {
    val key: String
    val value: String
}

sealed class HardConstraint(override val value: String) : EndpointKeyValue {
    class MeansLike(value: String) : HardConstraint(value) {
        override val key: String get() = "ml"
    }

    class SoundsLike(value: String) : HardConstraint(value) {
        override val key: String get() = "sl"
    }

    class SpelledLike(value: String) : HardConstraint(value) {
        override val key: String get() = "sp"
    }

    class RelatedWords(private val code: Code, value: String) : HardConstraint(value) {
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

        override val value: String
            get() = super.value
    }
}

class Topic(private vararg val topics: String) : EndpointKeyValue {
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

class LeftContext(private val leftContext: String) : EndpointKeyValue {
    override val key: String get() = "lc"
    override val value: String get() = leftContext
}

class RightContext(private val rightContext: String) : EndpointKeyValue {
    override val key: String get() = "rc"
    override val value: String get() = rightContext
}

class MaxResults(private val max: Int) : EndpointKeyValue {
    override val key: String get() = "max"
    override val value: String get() = max.toString()
}

class Metadata(private val flags: EnumSet<Flag>) : EndpointKeyValue {
    enum class Flag(val identifier: String) {
        DEFINITIONS("d"),
        PARTS_OF_SPEECH("p"),
        SYLLABLE_COUNT("s"),
        PRONUNCIATIONS("r"),
        WORD_FREQUENCY("f");

        infix fun and(other: Flag): EnumSet<Flag> = of(this, other)
    }

    override val key: String get() = "md"
    override val value: String
        get() = buildString {
            for (flag in flags) {
                append(flag.identifier)
            }
        }
}

infix fun EnumSet<Metadata.Flag>.and(other: Metadata.Flag): EnumSet<Metadata.Flag> =
    of(other, *this.toTypedArray())
