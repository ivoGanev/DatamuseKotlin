package com.ivo.ganev.datamusekotlin.core



import com.ivo.ganev.datamusekotlin.endpoint.words.HardConstraint.RelatedWords.Code.*
import com.ivo.ganev.datamusekotlin.endpoint.words.and
import com.ivo.ganev.datamusekotlin.configuration.buildWordsEndpointUrl
import com.ivo.ganev.datamusekotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamusekotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamusekotlin.exceptions.UnspecifiedHardConstraintException

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
        } catch (e: UnspecifiedHardConstraintException) {
        }

        // words with a meaning similar to ringing in the ears
        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.MeansLike("ringing in the ears")
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "ml=ringing%20in%20the%20ears"

        // words sounding like duck and rice to a maximum of 20
        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.SoundsLike("duck and rice")
            maxResults = 20
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "sl=duck%20and%20rice&max=20"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.SpelledLike("men")
            leftContext = "sweet"
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "sp=men&lc=sweet"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(POPULAR_ADJECTIVES, "sea")
            rightContext = "awake"
            leftContext = "mate"
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_jjb=sea&lc=mate&rc=awake"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(ANTONYMS, "ocean")
            topics = "temperature"
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_ant=ocean&topics=temperature"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(ANTONYMS, "girl")
            metadata = METADATA_ALL
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_ant=girl&md=dpsrf"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(COMPRISES, "complete test")
            leftContext = "left"
            rightContext = "right"
            topics = "topic"
            maxResults = 101
            metadata = METADATA_B
        }.buildUrl() shouldBeEqualTo ENDPOINTS_URL + "rel_com=complete%20test&topics=topic&lc=left&rc=right&max=101&md=dp"
    }
}