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
package com.ivo.ganev.datamuse_kotlin.feature.api

import com.ivo.ganev.datamuse_kotlin.feature.api.ConstraintElement.RelatedWordsElement
import com.ivo.ganev.datamuse_kotlin.feature.api.ConstraintElement.RelatedWordsElement.create
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.RelatedWords.Code
import com.ivo.ganev.datamuse_kotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamuse_kotlin.extenstions.string
import com.ivo.ganev.datamuse_kotlin.databinding.DatamuseDemoActivityBinding
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

    override fun constraint(): Set<HardConstraint> {
        val spinnerConstraint = binding.hardConstraintSpinner.selectedItem as ConstraintElement
        val spinnerCode = binding.relatedWordsSpinner.selectedItem as String
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