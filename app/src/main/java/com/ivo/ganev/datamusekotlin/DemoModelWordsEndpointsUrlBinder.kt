package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.ConstraintElement.RelatedWordsElement
import com.ivo.ganev.datamusekotlin.ConstraintElement.RelatedWordsElement.create
import com.ivo.ganev.datamusekotlin.api.HardConstraint
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code
import com.ivo.ganev.datamusekotlin.api.MetadataFlag
import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.string
import java.util.*

/**
 * This class is an example of how you can directly bind your UI data to the Datamuse URL builder.
 * Although it is not a requirement to use the Datamuse Kotlin Library with it, it shows one way
 * of working with the library
 * */
class DemoModelWordsEndpointsUrlBinder(private val binding: DatamuseDemoActivityBinding) :
    ModelWordsEndpointsUrlBinder() {

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

    override fun getConstraint(): HardConstraint {
        val spinnerConstraint = binding.constraintSpinner.selectedItem as ConstraintElement
        val spinnerCode = binding.constraintRelSpinner.selectedItem as String
        val code = RelatedWordsElement.codeMap[spinnerCode] ?: Code.APPROXIMATE_RHYMES
        val keyword = binding.etWord.string()

        return when (spinnerConstraint) {
            is RelatedWordsElement -> create(code, keyword)
            else -> spinnerConstraint.create(keyword)
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

    override fun getMetadata(): EnumSet<MetadataFlag> {
        val flags = metadataCheckbox.filter { it.key.isChecked }.values
        val set = EnumSet.noneOf(MetadataFlag::class.java)
        set.addAll(flags)
        return set
    }
}
