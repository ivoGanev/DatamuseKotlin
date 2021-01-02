package com.ivo.ganev.datamusekotlin

import androidx.test.platform.app.InstrumentationRegistry
import com.ivo.ganev.datamusekotlin.api.format
import com.ivo.ganev.datamusekotlin.extensions.readAssetFile
import com.ivo.ganev.datamusekotlin.core.WordResponse
import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.*
import com.ivo.ganev.datamusekotlin.core.KotlinJsonWordDecoder
import kotlinx.serialization.SerializationException
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class DatamuseKotlinWordRequestDecoderTest {
    companion object RequestFile {
        const val DEFINITIONS = "word-request-metadata-definitions.json"
        const val SIMPLE = "word-request-simple.json"
        const val SYLLABLES = "word-request-syllables.json"
        const val TAGS = "word-request-tags.json"
    }

    private lateinit var firstResponseElement: WordResponse
    private lateinit var lastResponseElement: WordResponse

    private var firstWord: Word? = null
    private var firstScore: Score? = null
    private var firstNumSyllablesCount: SyllablesCount? = null
    private var firstTags: Tags? = null
    private var firstDefinitions: Definitions? = null
    private var firstDefHeadwords: DefHeadwords? = null

    private var lastWord: Word? = null
    private var lastScore: Score? = null
    private var lastNumSyllablesCount: SyllablesCount? = null
    private var lastTags: Tags? = null
    private var lastDefinitions: Definitions? = null
    private var lastDefHeadwords: DefHeadwords? = null

    private lateinit var wordDecoder: KotlinJsonWordDecoder

    @Before
    fun setUp() {
        wordDecoder = KotlinJsonWordDecoder()
        firstResponseElement =  WordResponse(emptySet())
        lastResponseElement =  WordResponse(emptySet())
    }

    @Test
    fun wordDecoderWithMalformedJsonBodyThrowsException() {
        try {
            val wordResponseSet = wordDecoder.decode("malformed url")
            fail("Didn't throw")
        }
        catch (e: SerializationException){

        }
    }

    @Test
    fun wordDecoderDecodesCorrectlyTagRequests() {
        // Arrange
        val file = InstrumentationRegistry.getInstrumentation()
            .targetContext
            .assets
            .readAssetFile(TAGS)

        // Act - make word decoder to decode the provided JSON file
        val wordResponseSet = wordDecoder.decode(file)
        bindResponse(wordResponseSet)

        // Assert
        wordResponseSet.size shouldBeEqualTo 7

        firstWord shouldBeEqualTo Word("fretful")
        firstScore shouldBeEqualTo Score(398)
        firstNumSyllablesCount shouldBeEqualTo SyllablesCount(2)
        firstTags!!.tags[0] shouldBeEqualTo "pron:F R EH1 T F AH0 L "
        firstTags!!.tags[1] shouldBeEqualTo "f:0.573512"
        firstTags!!.tags.size shouldBeEqualTo 2
        firstDefinitions shouldBe null
        firstDefHeadwords shouldBe null

        lastWord shouldBeEqualTo Word("set fill")
        lastScore shouldBeEqualTo null
        lastNumSyllablesCount shouldBeEqualTo SyllablesCount(2)
        lastTags!!.tags[0] shouldBeEqualTo "pron:S EH1 T F IH0 L "
        lastTags!!.tags[1] shouldBeEqualTo "f:0.000000"
        lastTags!!.tags.size shouldBeEqualTo 2
        lastDefinitions shouldBe null
        lastDefHeadwords shouldBe null
    }

    @Test
    fun wordDecoderDecodesCorrectlyDefinitionsRequests() {
        // Arrange
        val file = InstrumentationRegistry.getInstrumentation()
            .targetContext
            .assets
            .readAssetFile(DEFINITIONS)

        // Act - make word decoder to decode the provided JSON file
        val wordResponseSet = wordDecoder.decode(file)
        bindResponse(wordResponseSet)

        // Assert
        wordResponseSet.size shouldBeEqualTo 21

        firstWord shouldBeEqualTo Word("birds")
        firstScore shouldBeEqualTo Score(700)
        firstNumSyllablesCount shouldBeEqualTo SyllablesCount(1)
        firstTags shouldBe null
        firstDefinitions!!.defs.first() shouldBeEqualTo   "n\twarm-blooded egg-laying vertebrates characterized by feathers and forelimbs modified as wings"
        firstDefinitions!!.defs.last() shouldBeEqualTo     "v\twatch and study birds in their natural habitat"
        firstDefHeadwords shouldBeEqualTo DefHeadwords("bird")

        lastWord shouldBeEqualTo Word("twothirds")
        lastScore shouldBe null
        lastNumSyllablesCount shouldBeEqualTo SyllablesCount(2)
        lastTags shouldBe null
        lastDefinitions shouldBe null
        lastDefHeadwords shouldBe null

        firstDefinitions!!.format()
    }

    private fun bindResponse(wordResponseSet: Set<WordResponse>) {
        firstResponseElement = wordResponseSet.first()
        lastResponseElement = wordResponseSet.last()

        firstWord = firstResponseElement[Word::class]
        firstScore = firstResponseElement[Score::class]
        firstNumSyllablesCount = firstResponseElement[SyllablesCount::class]
        firstTags = firstResponseElement[Tags::class]
        firstDefinitions = firstResponseElement[Definitions::class]
        firstDefHeadwords = firstResponseElement[DefHeadwords::class]

        lastWord = lastResponseElement[Word::class]
        lastScore = lastResponseElement[Score::class]
        lastNumSyllablesCount = lastResponseElement[SyllablesCount::class]
        lastTags = lastResponseElement[Tags::class]
        lastDefinitions = lastResponseElement[Definitions::class]
        lastDefHeadwords = lastResponseElement[DefHeadwords::class]
    }
}

