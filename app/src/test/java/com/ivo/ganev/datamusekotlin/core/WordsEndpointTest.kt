package com.ivo.ganev.datamusekotlin.core


import android.os.Build
import com.ivo.ganev.datamusekotlin.core.HardConstraint.*
import com.ivo.ganev.datamusekotlin.core.HardConstraint.RelatedWords.Code.*
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WordsEndpointTest {
    companion object {
        const val ENDPOINTS_URL = "https://api.datamuse.com/words?"
        val METADATA_A = MetadataFlag.DEFINITIONS
        val METADATA_B = METADATA_A and MetadataFlag.PARTS_OF_SPEECH
        val METADATA_C = METADATA_B and MetadataFlag.PRONUNCIATIONS
        val METADATA_ALL = METADATA_C and MetadataFlag.SYLLABLE_COUNT and MetadataFlag.WORD_FREQUENCY
    }

    @Test
    fun testIfUrlComesOutCorrect() {
        // words with a meaning similar to ringing in the ears
        buildWordsEndpointUrl {
            hardConstraint = MeansLike("ringing in the ears")
        } shouldBeEqualTo ENDPOINTS_URL + "ml=ringing%20in%20the%20ears"

        // words sounding like duck and rice to a maximum of 20
        buildWordsEndpointUrl {
            hardConstraint = SoundsLike("duck and rice")
            maxResults = 20
        } shouldBeEqualTo ENDPOINTS_URL + "sl=duck%20and%20rice&max=20"

        buildWordsEndpointUrl {
            hardConstraint = SpelledLike("men")
            leftContext = "sweet"
        } shouldBeEqualTo ENDPOINTS_URL + "sp=men&lc=sweet"

        buildWordsEndpointUrl {
            hardConstraint = RelatedWords(POPULAR_ADJECTIVES ,"sea")
            rightContext = "awake"
            leftContext = "mate"
        } shouldBeEqualTo ENDPOINTS_URL + "rel_jjb=sea&lc=mate&rc=awake"

        buildWordsEndpointUrl {
            hardConstraint = RelatedWords(ANTONYMS ,"ocean")
            topics = "temperature"
        } shouldBeEqualTo ENDPOINTS_URL + "rel_ant=ocean&topics=temperature"

        buildWordsEndpointUrl {
            hardConstraint = RelatedWords(ANTONYMS ,"girl")
            metadata = METADATA_ALL
        } shouldBeEqualTo ENDPOINTS_URL + "rel_ant=girl&md=dpsrf"

        buildWordsEndpointUrl {
            hardConstraint = RelatedWords(COMPRISES ,"complete test")
            leftContext = "left"
            rightContext = "right"
            topics = "topic"
            maxResults = 101
            metadata = METADATA_B
        } shouldBeEqualTo ENDPOINTS_URL + "rel_com=complete%20test&topics=topic&lc=left&rc=right&max=101&md=dp"
    }
}