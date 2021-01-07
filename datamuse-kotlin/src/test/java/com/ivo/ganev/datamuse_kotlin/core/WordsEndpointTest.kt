package com.ivo.ganev.datamuse_kotlin.core



import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.RelatedWords.Code.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.and
import com.ivo.ganev.datamuse_kotlin.configuration.buildWordsEndpointUrl
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.SpelledLike
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.MeansLike
import com.ivo.ganev.datamuse_kotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamuse_kotlin.endpoint.words.hardConstraintsOf
import com.ivo.ganev.datamuse_kotlin.exceptions.IllegalHardConstraintState

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import kotlin.test.fail


class WordsEndpointTest {
    companion object {
        const val ENDPOINTS_URL = "https://api.datamuse.com/words?"
        private val METADATA_A = MetadataFlag.DEFINITIONS
        private val METADATA_B = METADATA_A and MetadataFlag.PARTS_OF_SPEECH
        private val METADATA_C = METADATA_B and MetadataFlag.PRONUNCIATIONS
        private val METADATA_ALL = METADATA_C and MetadataFlag.SYLLABLE_COUNT and MetadataFlag.WORD_FREQUENCY
    }

    @Test
    fun testIfUrlComesOutCorrect() {
        // test for a hard constraint
        try {
            buildWordsEndpointUrl {}
            fail()
        } catch (e: IllegalHardConstraintState) {
        }

        // Tests for:
        // single hard constraint
        buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(MeansLike("ringing in the ears"))
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "ml=ringing%20in%20the%20ears"

        // infix "and" for hard constraints
        buildWordsEndpointUrl {
            hardConstraints = MeansLike("ringing in the ears") and
                    SoundsLike("a*") and
                    RelatedWords(ANTONYMS, "related")
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "ml=ringing%20in%20the%20ears&sl=a*&rel_ant=related"

        // max results
        buildWordsEndpointUrl {
            hardConstraints =  hardConstraintsOf(SoundsLike("duck and rice"))
            maxResults = 20
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "sl=duck%20and%20rice&max=20"

        // left context
        buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(SpelledLike("men"))
            leftContext = "sweet"
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "sp=men&lc=sweet"

        // left and right context
        buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(RelatedWords(POPULAR_ADJECTIVES, "sea"))
            rightContext = "awake"
            leftContext = "mate"
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_jjb=sea&lc=mate&rc=awake"

        // topics
        buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(RelatedWords(ANTONYMS, "ocean"))
            topics = "temperature"
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_ant=ocean&topics=temperature"

        // metadata infix "and"
        buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(RelatedWords(ANTONYMS, "girl"))
            metadata = METADATA_ALL
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_ant=girl&md=dpsrf"

        // full words endpoint test
        buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(RelatedWords(COMPRISES, "complete test"))
            leftContext = "left"
            rightContext = "right"
            topics = "topic"
            maxResults = 101
            metadata = METADATA_B
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_com=complete%20test&topics=topic&lc=left&rc=right&max=101&md=dp"
    }
}