package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.UiConstraintElement.*
import com.ivo.ganev.datamusekotlin.api.HardConstraint
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code.APPROXIMATE_RHYMES
import com.ivo.ganev.datamusekotlin.api.MetadataFlag
import com.ivo.ganev.datamusekotlin.api.Topic
import com.ivo.ganev.datamusekotlin.core.WordResponse
import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.*

import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId
import com.ivo.ganev.datamusekotlin.extenstions.string
import java.util.*


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener {
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var apiData: ApiData

    private val viewModel: DatamuseActivityViewModel by viewModels()

    private val constraints = listOf(
        MeansLikeElement, SoundsLikeElement, SpelledLikeElement, RelatedWordsElement
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DatamuseDemoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerAdapter = SpinnerAdapterHardConstraint(this, constraints)
        binding.tvResponse.movementMethod = ScrollingMovementMethod()
        binding.constraintSpinner.adapter = spinnerAdapter
        apiData = ApiData(binding)

        viewModel.result.observe(this, {
            var i = 1
            binding.tvResponse.text = ""

            it.forEach { wordResponse ->
                val word = wordResponse[Word::class]?.word
                val score = wordResponse[Score::class]?.score
                val definitions  = wordResponse[Definitions::class]?.defs
                val tags = wordResponse[Tags::class]?.tags
                val syllablesCount = wordResponse[SyllablesCount::class]?.numSyllables
                val defHeadwords = wordResponse[DefHeadwords::class]?.defHeadword

                binding.tvResponse.append("$i. $word , Score: $score, Definitions: $definitions, Tags: $tags, Syllable Count: $syllablesCount, Def Headwords: $defHeadwords\n")
                i++
            } })
    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            viewModel.makeNetworkRequest(apiData.toUrlString())
        }
    }

    class ApiData(private val binding: DatamuseDemoActivityBinding) : DatamuseActivityData() {

        private val metadataCheckbox = mapOf(binding.cbDefinitions to MetadataFlag.DEFINITIONS,
                binding.cbPartsOfSpeech to MetadataFlag.PARTS_OF_SPEECH,
                binding.cbPronunciation to MetadataFlag.PRONUNCIATIONS,
                binding.cbSyllableCount to MetadataFlag.SYLLABLE_COUNT,
                binding.cbWordFrequency to MetadataFlag.WORD_FREQUENCY)

        override fun getConstraint(): HardConstraint {
            val constraintElement = binding.constraintSpinner.selectedItem as UiConstraintElement
            val keyword = binding.etWord.string()

            return when (constraintElement) {
                is RelatedWordsElement -> constraintElement.create(APPROXIMATE_RHYMES, keyword)
                else -> constraintElement.create(keyword)
            }
        }

        override fun getTopics(): String {
            return binding.etTopics.string()
        }

        override fun getLeftContext(): String {
            return binding.etLc.string()
        }

        override fun getRightContext(): String {
            return binding.etRc.string()
        }

        override fun getMaxResults(): Int {
            return Integer.parseInt(binding.etMaxResults.string())
        }

        override fun getMetadata(): EnumSet<MetadataFlag>? {
            val flags = metadataCheckbox.filter { it.key.isChecked }.values
            val set = EnumSet.noneOf(MetadataFlag::class.java)
            set.addAll(flags)
            return set
        }
    }
}




