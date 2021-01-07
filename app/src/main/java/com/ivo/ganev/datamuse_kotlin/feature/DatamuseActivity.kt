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
package com.ivo.ganev.datamuse_kotlin.feature

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamuse_kotlin.R
import com.ivo.ganev.datamuse_kotlin.common.buildToString
import com.ivo.ganev.datamuse_kotlin.common.format
import com.ivo.ganev.datamuse_kotlin.configuration.buildWordsEndpointUrl
import com.ivo.ganev.datamuse_kotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint.RelatedWords
import com.ivo.ganev.datamuse_kotlin.endpoint.words.Metadata
import com.ivo.ganev.datamuse_kotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamuse_kotlin.endpoint.words.WordResponse.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.WordResponse.Element.*
import com.ivo.ganev.datamuse_kotlin.endpoint.words.and
import com.ivo.ganev.datamuse_kotlin.extenstions.isWithId
import com.ivo.ganev.datamuse_kotlin.feature.adapter.HardConstraintAdapter
import com.ivo.ganev.datamuse_kotlin.feature.api.ConstraintElement.*
import com.ivo.ganev.datamuse_kotlin.feature.api.UrlModel


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var constraintAdapter: SpinnerAdapter
    private lateinit var modelUrlBuilder: UrlModel

    private val viewModel: DatamuseActivityViewModel by viewModels()

    private val constraints = listOf(
        MeansLikeElement, SoundsLikeElement, SpelledLikeElement, RelatedWordsElement
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DatamuseDemoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        constraintAdapter = HardConstraintAdapter(this, constraints)
        binding.hardConstraintSpinner.onItemSelectedListener = this
        binding.hardConstraintSpinner.adapter = constraintAdapter
        binding.tvResponse.movementMethod = ScrollingMovementMethod()
        binding.relatedWordsSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, RelatedWordsElement.codeMap.keys.toList())
        modelUrlBuilder = UrlModel(binding)

        viewModel.result.observe(this, { wordResponseSet ->
            var i = 0
            binding.tvResponse.text = ""

            wordResponseSet.forEach {
                val word = it[Word::class]?.word
                val score = it[Score::class]?.score
                val definitions = it[Definitions::class]?.format()?.buildToString()
                val tags = it[Tags::class]?.tags
                val syllablesCount = it[SyllablesCount::class]?.numSyllables
                val defHeadwords = it[DefHeadwords::class]?.defHeadword

                val jsonFormattedOutput = buildString {
                    append("${++i}. $word\n")
                    if (score != null)
                        append("Score: $score\n")
                    if (definitions != null)
                        append("Definitions: $definitions")
                    if (tags != null)
                        append("Tags: $tags\n")
                    if (syllablesCount != null)
                        append("Syllable Count: $syllablesCount\n")
                    if (defHeadwords != null)
                        append("Def Headwords: $defHeadwords")
                    append("\n\n")
                }
                binding.tvResponse.append(jsonFormattedOutput)
            }
        })

        viewModel.failure.observe(this, {
            binding.tvResponse.text = it.toString()
        })

        // There should be global response providing a parse function
        // and extracting each element

    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_query) {
            val config = modelUrlBuilder.build()
            binding.tvUrl.text = config.buildUrl()
            viewModel.makeNetworkRequest(modelUrlBuilder.build())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent isWithId R.id.hard_constraint_spinner) {
            binding.relatedWordsSpinner.visibility = when (constraints[position]) {
                is RelatedWordsElement -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}




