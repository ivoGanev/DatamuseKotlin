/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
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
package com.ivo.ganev.datamuse_kotlin.feature

import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.RelatedWords.Code

/**
 * A class representing a single element of type [HardConstraint] with a label.
 * This is not part of the Datamuse library.
 *
 * What is it used for?
 *
 * Each [ConstraintElement] has a label which the Spinner uses to
 * display in the Activity. A list of [ConstraintElement]'s can be put inside
 * a spinner adapter and whenever necessary to create a [HardConstraint]
 * from it.
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