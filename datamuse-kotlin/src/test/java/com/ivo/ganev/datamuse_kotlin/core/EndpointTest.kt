package com.ivo.ganev.datamuse_kotlin.core



import com.ivo.ganev.datamuse_kotlin.endpoint.builders.sugBuilder
import com.ivo.ganev.datamuse_kotlin.endpoint.builders.wordsBuilder
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.RelatedWords.Code.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.and
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.SpelledLike
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.MeansLike
import com.ivo.ganev.datamuse_kotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamuse_kotlin.endpoint.words.hardConstraintsOf
import com.ivo.ganev.datamuse_kotlin.exceptions.IllegalHardConstraintState

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import kotlin.test.fail


class EndpointTest {
    companion object {
        const val WORDS_ENDPOINTS_URL = "https://api.datamuse.com/words?"
        const val SUG_ENDPOINTS_URL = "https://api.datamuse.com/sug"
        private val METADATA_A = MetadataFlag.DEFINITIONS
        private val METADATA_B = METADATA_A and MetadataFlag.PARTS_OF_SPEECH
        private val METADATA_C = METADATA_B and MetadataFlag.PRONUNCIATIONS
        private val METADATA_ALL = METADATA_C and MetadataFlag.SYLLABLE_COUNT and MetadataFlag.WORD_FREQUENCY
    }

    @Test
    fun `test if sug endpoint url comes out correct`() {
        sugBuilder {
            hint = ""
        }.build().toUrl() shouldBeEqualTo SUG_ENDPOINTS_URL

        sugBuilder {
            hint = "sw"
            maxResults = 10
        }.build().toUrl() shouldBeEqualTo "$SUG_ENDPOINTS_URL?s=sw&max=10"
    }

    @Test
    fun `test if words endpoint comes out correct`() {
        val wordsBuilderTest = ::wordsBuilder

        // if no hard constraint were provided the test should fail
        try {
            wordsBuilderTest {}
            fail()
        } catch (e: IllegalHardConstraintState) {
        }

        // Tests for:
        // single hard constraint
        wordsBuilderTest {
            hardConstraints = hardConstraintsOf(MeansLike("ringing in the ears"))
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "ml=ringing%20in%20the%20ears"

        // infix "and" for hard constraints
        wordsBuilderTest {
            hardConstraints = MeansLike("ringing in the ears") and
                    SoundsLike("a*") and
                    RelatedWords(ANTONYMS, "related")
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "ml=ringing%20in%20the%20ears&sl=a*&rel_ant=related"

        // max results
        wordsBuilderTest {
            hardConstraints =  hardConstraintsOf(SoundsLike("duck and rice"))
            maxResults = 20
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "sl=duck%20and%20rice&max=20"

        // left context
        wordsBuilderTest {
            hardConstraints = hardConstraintsOf(SpelledLike("men"))
            leftContext = "sweet"
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "sp=men&lc=sweet"

        // left and right context
        wordsBuilderTest {
            hardConstraints = hardConstraintsOf(RelatedWords(POPULAR_ADJECTIVES, "sea"))
            rightContext = "awake"
            leftContext = "mate"
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "rel_jjb=sea&lc=mate&rc=awake"

        // topics
        wordsBuilderTest {
            hardConstraints = hardConstraintsOf(RelatedWords(ANTONYMS, "ocean"))
            topics = "temperature"
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "rel_ant=ocean&topics=temperature"

        // metadata infix "and"
        wordsBuilderTest {
            hardConstraints = hardConstraintsOf(RelatedWords(ANTONYMS, "girl"))
            metadata = METADATA_ALL
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "rel_ant=girl&md=dpsrf"

        // full words endpoint test
        wordsBuilderTest {
            hardConstraints = hardConstraintsOf(RelatedWords(COMPRISES, "complete test"))
            leftContext = "left"
            rightContext = "right"
            topics = "topic"
            maxResults = 101
            metadata = METADATA_B
        }.build().toUrl() shouldBeEqualTo WORDS_ENDPOINTS_URL + "rel_com=complete%20test&topics=topic&lc=left&rc=right&max=101&md=dp"
    }
}