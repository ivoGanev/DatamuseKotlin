package com.ivo.ganev.datamusekotlin.core


import android.os.Build
import com.ivo.ganev.datamusekotlin.api.HardConstraint
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code.*
import com.ivo.ganev.datamusekotlin.api.MetadataFlag
import com.ivo.ganev.datamusekotlin.api.and
import com.ivo.ganev.datamusekotlin.api.buildWordsEndpointUrl

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.fail

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
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
            buildWordsEndpointUrl{}
            fail()
        }
        catch (e: UnspecifiedHardConstraintException) {}

        // words with a meaning similar to ringing in the ears
        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.MeansLike("ringing in the ears")
        } shouldBeEqualTo ENDPOINTS_URL + "ml=ringing%20in%20the%20ears"

        // words sounding like duck and rice to a maximum of 20
        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.SoundsLike("duck and rice")
            maxResults = 20
        } shouldBeEqualTo ENDPOINTS_URL + "sl=duck%20and%20rice&max=20"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.SpelledLike("men")
            leftContext = "sweet"
        } shouldBeEqualTo ENDPOINTS_URL + "sp=men&lc=sweet"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(POPULAR_ADJECTIVES, "sea")
            rightContext = "awake"
            leftContext = "mate"
        } shouldBeEqualTo ENDPOINTS_URL + "rel_jjb=sea&lc=mate&rc=awake"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(ANTONYMS, "ocean")
            topics = "temperature"
        } shouldBeEqualTo ENDPOINTS_URL + "rel_ant=ocean&topics=temperature"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(ANTONYMS, "girl")
            metadata = METADATA_ALL
        } shouldBeEqualTo ENDPOINTS_URL + "rel_ant=girl&md=dpsrf"

        buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(COMPRISES, "complete test")
            leftContext = "left"
            rightContext = "right"
            topics = "topic"
            maxResults = 101
            metadata = METADATA_B
        } shouldBeEqualTo ENDPOINTS_URL + "rel_com=complete%20test&topics=topic&lc=left&rc=right&max=101&md=dp"
    }
}