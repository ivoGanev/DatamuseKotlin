package com.ivo.ganev.datamusekotlin.api


import com.ivo.ganev.datamusekotlin.core.EndpointKeyValue
import com.ivo.ganev.datamusekotlin.core.UnspecifiedHardConstraintException
import com.ivo.ganev.datamusekotlin.core.WordsEndpointsUrlBuilder
import java.util.*
import java.util.EnumSet.of

typealias MetadataFlag = Metadata.Flag

/**
 * Builds the URL address for the Datamuse API. In order to be able to build a URL you need to
 * provide at least a hard constraint.
 * 
 * @throws UnspecifiedHardConstraintException - when no hard constraint is specified
 * */
fun buildWordsEndpointUrl(endpointConfig: WordsEndpointBuilder.() -> Unit): String {
    val builder = WordsEndpointBuilder()
    builder.endpointConfig()
    val buildConfig = builder.build()
    val path = with(buildConfig) {
        if(hardConstraint==null)
            throw UnspecifiedHardConstraintException(
                "You need to provide a hard constraint in order to build a URL for the API"
            )
        listOf(hardConstraint, topic, leftContext, rightContext, maxResults, metadata)
    }
    return WordsEndpointsUrlBuilder(path).build()
}

sealed class HardConstraint(override val value: String) : EndpointKeyValue {

    /**
     * 	Means like constraint: require that the results have a meaning related to this string
     * 	value, which can be any word or sequence of words. (This is effectively the reverse
     * 	dictionary feature of OneLook.)
     * */
    class MeansLike(value: String) : HardConstraint(value) {
        override val key: String get() = "ml"
    }

    /**
     * Sounds like constraint: require that the results are pronounced similarly to this string
     * of characters. (If the string of characters doesn't have a known pronunciation, the system
     * will make its best guess using a text-to-phonemes algorithm.)
     * */
    class SoundsLike(value: String) : HardConstraint(value) {
        override val key: String get() = "sl"
    }

    /**
     * Spelled like constraint: require that the results are spelled similarly to this string of
     * characters, or that they match this wildcard pattern. A pattern can include any combination
     * of alphanumeric characters, spaces, and two reserved characters that represent placeholders
     * — * (which matches any number of characters) and ? (which matches exactly one character).
     * */
    class SpelledLike(value: String) : HardConstraint(value) {
        override val key: String get() = "sp"
    }

    /**
     * Related word constraints: require that the results, when paired with the word in this
     * parameter, are in a predefined lexical relation indicated by [code]. Any number of these
     * parameters may be specified any number of times. An assortment of semantic, phonetic, and
     * corpus-statistics-based relations are available. At this time, these relations are available
     * for English-language vocabularies only.
     * */
    class RelatedWords(private val code: Code, value: String) : HardConstraint(value) {
        enum class Code(val value: String) {
            /**
             * Description: Popular nouns modified by the given adjective, per Google Books Ngrams
             *
             * Example: gradual → increase
             * */
            POPULAR_NOUNS("jja"),

            /**
             * Description: Popular adjectives used to modify the given noun, per Google Books Ngrams
             *
             * Example: beach → sandy
             * */
            POPULAR_ADJECTIVES("jjb"),

            /**
             * Description: Synonyms (words contained within the same WordNet synset)
             *
             * Example: ocean → sea
             * */
            SYNONYMS("syn"),

            /**
             * Description: "Triggers" (words that are statistically associated with the
             * query word in the same piece of text.)
             *
             * Example: cow → milking
             * */
            TRIGGERS("trg"),

            /**
             * Description: Antonyms (per WordNet)
             *
             * Example: late → early
             * */
            ANTONYMS("ant"),

            /**
             * Description: "Kind of" (direct hypernyms, per WordNet)
             *
             * Example: gondola → boat
             * */
            KIND_OF("spc"),

            /**
             * Description: "More general than" (direct hyponyms, per WordNet)
             *
             * Example: boat → gondola
             * */
            MORE_GENERAL_THAN("gen"),

            /**
             * Description: "Comprises" (direct holonyms, per WordNet)
             *
             * Example: car → accelerator
             * */
            COMPRISES("com"),

            /**
             * Description: "Part of" (direct meronyms, per WordNet)
             *
             * Example: trunk → tree
             * */
            PART_OF("par"),

            /**
             * Description: Frequent followers (w′ such that P(w′|w) ≥ 0.001,
             * per Google Books Ngrams)
             *
             * Example: wreak → havoc
             * */
            FREQUENT_FOLLOWERS("bga"),

            /**
             * Description: Frequent predecessors (w′ such that P(w|w′) ≥ 0.001, per Google
             * Books Ngrams)
             *
             * Example: havoc → wreak
             * */
            FREQUENT_PREDECESSORS("bgb"),

            /**
             * Description: Rhymes ("perfect" rhymes, per RhymeZone)
             *
             * Example: spade → aid
             * */
            RHYMES("rhy"),

            /**
             * Description: Approximate rhymes (per RhymeZone)
             *
             * Example: forest → chorus
             * */
            APPROXIMATE_RHYMES("nry"),

            /**
             * Description: Homophones (sound-alike words)
             *
             * Example: course → coarse
             * */
            HOMOPHONES("hom"),

            /**
             * Description: Consonant match
             *
             * Example: sample → simple
             * */
            CONSONANT_MATCH("cns")
        }

        override val key: String
            get() = "rel_${code.value}"

        override val value: String
            get() = super.value
    }
}

/**
 * Topic words: An optional hint to the system about the theme of the document being written.
 * Results will be skewed toward these topics. At most 5 words can be specified.
 * Space or comma delimited. Nouns work best.
 * */
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

/**
 * Left context: An optional hint to the system about the word that
 * appears immediately to the left of the target word in a sentence.
 * (At this time, only a single word may be specified.)
 * */
class LeftContext(private val leftContext: String) : EndpointKeyValue {
    override val key: String get() = "lc"
    override val value: String get() = leftContext
}

/**
 * Right context: An optional hint to the system about the word that appears
 * immediately to the right of the target word in a sentence.
 * (At this time, only a single word may be specified.)
 * */
class RightContext(private val rightContext: String) : EndpointKeyValue {
    override val key: String get() = "rc"
    override val value: String get() = rightContext
}

/**
 * Maximum number of results to return, not to exceed 1000. (default: 100)
 * */
class MaxResults(private val max: Int = 100) : EndpointKeyValue {
    override val key: String get() = "max"
    override val value: String get() = max.toString()
}

/**
 * Metadata flags: A list of single-letter codes (no delimiter) requesting that extra
 * lexical knowledge be included with the results.
 *
 * The API makes an effort to ensure that metadata values are consistent with
 * the sense or senses of the word that best match the API query. For example,
 * the word "refuse" is tagged as a verb ("v") in the results of a search for
 * words related to "deny" but as a noun ("n") in the results of a search for
 * words related to "trash". And "resume" is shown to have 2 syllables in a search
 * of rhymes for "exhume" but 3 syllables in a search of rhymes for "macrame".
 * There are occasional errors in this guesswork, particularly with pronunciations.
 * Metadata is available for both English (default) and Spanish (v=es) vocabularies.
 * */
class Metadata(private val flags: EnumSet<Flag>) : EndpointKeyValue {
    enum class Flag(val identifier: String) {
        /**
         * Produced in the defs field of the result object. The definitions are from WordNet.
         * If the word is an inflected form (such as the plural of a noun or a conjugated form
         * of a verb), then an additional defHeadword field will be added indicating the base
         * form from which the definitions are drawn.
         * */
        DEFINITIONS("d"),

        /**
         * One or more part-of-speech codes will be added to the tags field of the result object.
         * "n" means noun, "v" means verb, "adj" means adjective, "adv" means adverb, and "u" means
         * that the part of speech is none of these or cannot be determined.
         * Multiple entries will be added when the word's part of speech is ambiguous,
         * with the most popular part of speech listed first. This field is derived from an
         * analysis of Google Books Ngrams data.
         * */
        PARTS_OF_SPEECH("p"),

        /**
         * Produced in the numSyllables field of the result object.
         * In certain cases the number of syllables may be ambiguous,
         * in which case the system's best guess is chosen based on the entire query.
         * */
        SYLLABLE_COUNT("s"),

        /**
         * Produced in the tags field of the result object, prefixed by "pron:".
         * This is the system's best guess for the pronunciation of the word or phrase.
         * The format of the pronunication is a space-delimited list of Arpabet phoneme codes.
         * If you add "&ipa=1" to your API query, the pronunciation string will instead use the
         * International Phonetic Alphabet. Note that for terms that are very rare or outside of
         * the vocabulary, the pronunciation will be guessed based on the spelling.
         * In certain cases the pronunciation may be ambiguous, in which case the system's best
         * guess is chosen based on the entire query.
         * */
        PRONUNCIATIONS("r"),

        /**
         * Produced in the tags field of the result object, prefixed by "f:".
         * The value is the number of times the word (or multi-word phrase)
         * occurs per million words of English text according to Google Books Ngrams.
         * */
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

/**
 * Combines metadata flags
 * */
infix fun EnumSet<Metadata.Flag>.and(other: Metadata.Flag): EnumSet<Metadata.Flag> =
    of(other, *this.toTypedArray())
