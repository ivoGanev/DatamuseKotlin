package com.ivo.ganev.datamusekotlin.feature.api

import com.ivo.ganev.datamusekotlin.feature.api.ConstraintElement.RelatedWordsElement
import com.ivo.ganev.datamusekotlin.feature.api.ConstraintElement.RelatedWordsElement.create
import com.ivo.ganev.datamusekotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamusekotlin.endpoint.words.HardConstraint.RelatedWords.Code
import com.ivo.ganev.datamusekotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.string
import java.util.*

/**
 * This class is an example of how you can build a URL for a Datamuse query by directly
 * binding your UI data from the activity to the lambda builder method. Here we use the
 * activity binding to override all the methods although getConstraint() is the only
 * necessary one.
 * */
class UrlModel(private val binding: DatamuseDemoActivityBinding) :
    UrlModelBase() {

    /**
     * Each checkbox from the activity corresponds to a MetadataFlag
     * */
    private val metadataCheckbox = mapOf(
        binding.cbDefinitions to MetadataFlag.DEFINITIONS,
        binding.cbPartsOfSpeech to MetadataFlag.PARTS_OF_SPEECH,
        binding.cbPronunciation to MetadataFlag.PRONUNCIATIONS,
        binding.cbSyllableCount to MetadataFlag.SYLLABLE_COUNT,
        binding.cbWordFrequency to MetadataFlag.WORD_FREQUENCY
    )

    override fun constraint(): HardConstraint {
        val spinnerConstraint = binding.constraintSpinner.selectedItem as ConstraintElement
        val spinnerCode = binding.constraintRelSpinner.selectedItem as String
        val code = RelatedWordsElement.codeMap[spinnerCode] ?: Code.APPROXIMATE_RHYMES
        val keyword = binding.etWord.string()

        return when (spinnerConstraint) {
            is RelatedWordsElement -> create(code, keyword)
            else -> spinnerConstraint.create(keyword)
        }
    }

    override fun topics(): String {
        return binding.etTopics.string()
    }

    override fun leftContext(): String {
        return binding.etLc.string()
    }

    override fun rightContext(): String {
        return binding.etRc.string()
    }

    override fun maxResults(): Int {
        return Integer.parseInt(binding.etMaxResults.string())
    }

    override fun metadata(): EnumSet<MetadataFlag> {
        // filter all checkboxes and extract the MetadataFlags's
        val flags = metadataCheckbox.filter { it.key.isChecked }.values

        val set = EnumSet.noneOf(MetadataFlag::class.java)
        set.addAll(flags)
        return set
    }
}
