package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.api.HardConstraint
import com.ivo.ganev.datamusekotlin.api.HardConstraint.*
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code

/**
 * A class representing a single element of type [HardConstraint] with a label. Usage:
 * Put object elements inside a list like:
 *```
 * val labeledConstraint = listof(RelatedWordsElement, SoundsLikeElement..)
 *```
 * and use them to quickly create a [HardConstraint] like:
 *```
 * val constraint = when (labeledConstraint[0]) {
 * is RelatedWordsElement -> labeledConstraint[0].create(APPROXIMATE_RHYMES, keyword)
 * else -> labeledConstraint[0].create(keyword)
 * }
 * println(constraint.toString() + " :: "  + constraint.key + " :: " + constraint.value)
 * ```
 * */
sealed class ConstraintElement(val label: String) {
    abstract fun create(word: String): HardConstraint

    object MeansLikeElement : ConstraintElement("Means Like") {
        override fun create(word: String): MeansLike = MeansLike(word)
    }

    object SoundsLikeElement : ConstraintElement("Sounds Like") {
        override fun create(word: String): SoundsLike = SoundsLike(word)
    }

    object SpelledLikeElement : ConstraintElement("Spelled Like") {
        override fun create(word: String): SpelledLike = SpelledLike(word)
    }

    object RelatedWordsElement : ConstraintElement("Related Words") {
        val codeMap = mapOf(
            "Approximate Rhymes" to Code.APPROXIMATE_RHYMES,
            "Antonyms" to Code.ANTONYMS,
            "Comprises" to Code.COMPRISES,
            "Consonant Match" to Code.CONSONANT_MATCH,
            "Frequent Followers" to Code.FREQUENT_FOLLOWERS,
            "Frequent Predecessors" to Code.FREQUENT_PREDECESSORS,
            "Homophobes" to Code.HOMOPHONES,
            "Kind of" to Code.KIND_OF,
            "More General Than" to Code.MORE_GENERAL_THAN,
            "Part of" to Code.PART_OF,
            "Popular Adjectives" to Code.POPULAR_ADJECTIVES,
            "Popular Nouns" to Code.POPULAR_NOUNS,
            "Rhymes" to Code.RHYMES,
            "Synonyms" to Code.SYNONYMS,
            "Triggers" to Code.TRIGGERS,
        )

        override fun create(word: String): HardConstraint = create(code = Code.APPROXIMATE_RHYMES, word)

        fun create(code: Code, word: String): RelatedWords = RelatedWords(code, word)
    }
}


//sealed class RelatedWordsCodeElement(val label: String) {
//
//}