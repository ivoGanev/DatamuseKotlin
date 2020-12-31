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
sealed class UiConstraintElement(val label: String) {
    abstract fun create(word: String): HardConstraint

    object MeansLikeElement : UiConstraintElement("Means Like") {
        override fun create(word: String): HardConstraint.MeansLike {
            return MeansLike(word)
        }
    }

    object SoundsLikeElement : UiConstraintElement("Sounds Like") {
        override fun create(word: String): HardConstraint.SoundsLike {
            return SoundsLike(word)
        }
    }

    object SpelledLikeElement : UiConstraintElement("Spelled Like") {
        override fun create(word: String): HardConstraint.SpelledLike {
            return SpelledLike(word)
        }
    }

    object RelatedWordsElement : UiConstraintElement("Related Words") {
        override fun create(word: String): HardConstraint {
            return create(code = Code.APPROXIMATE_RHYMES, word)
        }

        fun create(code: Code, word: String): HardConstraint.RelatedWords {
            return RelatedWords(code, word)
        }
    }
}