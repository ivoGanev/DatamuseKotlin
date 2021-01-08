/**
 * Copyright (C) 2020 Ivo Ganev
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
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamuse_kotlin.R
import com.ivo.ganev.datamuse_kotlin.common.string
import com.ivo.ganev.datamuse_kotlin.common.format
import com.ivo.ganev.datamuse_kotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamuse_kotlin.endpoint.builders.wordsBuilder
import com.ivo.ganev.datamuse_kotlin.endpoint.words.MetadataFlag
import com.ivo.ganev.datamuse_kotlin.response.WordResponse.Element.*
import com.ivo.ganev.datamuse_kotlin.exceptions.IllegalHardConstraintState
import com.ivo.ganev.datamuse_kotlin.extenstions.isWithId
import com.ivo.ganev.datamuse_kotlin.extenstions.string
import com.ivo.ganev.datamuse_kotlin.extenstions.toInt
import com.ivo.ganev.datamuse_kotlin.feature.adapter.HardConstraintAdapter
import com.ivo.ganev.datamuse_kotlin.feature.ConstraintElement.*
import com.ivo.ganev.datamuse_kotlin.feature.ConstraintElement.RelatedWordsElement.codeMap
import java.util.*

/**
 * A little more involved demo activity to illustrate how the library can be used.
 * */
class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var metadataCheckboxMap: Map<MetadataFlag, CheckBox>
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var constraintAdapter: HardConstraintAdapter

    private val viewModel: DatamuseActivityViewModel by viewModels()

    private fun configure() {
        constraintAdapter = HardConstraintAdapter(this)
        binding.btnAddConstraint.setOnClickListener(this)
        binding.hardConstraintSpinner.adapter = constraintAdapter
        binding.hardConstraintSpinner.onItemSelectedListener = this

        binding.tvResponse.movementMethod = ScrollingMovementMethod()
        binding.relatedWordsSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, codeMap.keys.toList())

        /*
        *  Each MetadataFlag from the activity corresponds to a checkbox.
        * */
        metadataCheckboxMap = mapOf(
            MetadataFlag.DEFINITIONS to binding.cbDefinitions,
            MetadataFlag.PARTS_OF_SPEECH to binding.cbPartsOfSpeech,
            MetadataFlag.PRONUNCIATIONS to binding.cbPronunciation,
            MetadataFlag.SYLLABLE_COUNT to binding.cbSyllableCount,
            MetadataFlag.WORD_FREQUENCY to binding.cbWordFrequency
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DatamuseDemoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configure()

        viewModel.result.observe(this, { wordResponseSet ->
            var i = 0
            binding.tvResponse.text = ""

            wordResponseSet.forEach {
                // Yet another way to extract all the elements of the words
                // from the response. If checking for null is not the most
                // appealing idea, see [SimpleDemoActivity] how to use "when"
                // to avoid it.
                val word = it[Word::class]?.word
                val score = it[Score::class]?.score
                val definitions = it[Definitions::class]?.format()?.string()
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

        viewModel.url.observe(this, {
            binding.tvUrl.text = it
        })
    }

    override fun onClick(v: View?) {
        when {
            v isWithId R.id.btn_query -> onQuery()
            v isWithId R.id.btn_add_constraint -> onAddConstraint()
        }
    }

    private fun onAddConstraint() {
        val spinnerPosition = binding.hardConstraintSpinner.selectedItemPosition
        val word = binding.etWord.string()
        val constraint = constraintAdapter.constraints[spinnerPosition].create(word)
        binding.cgHardConstraints.addConstraint(constraint)
    }

    /**
     * Collects all the data from the activity's views and passes it on to
     * the view model to make a query.
     * */
    private fun onQuery() {
        val hardConstraints = binding.cgHardConstraints.getConstraints()
        val topics = binding.etTopics.string()
        val leftContext = binding.etLc.string()
        val rightContext = binding.etRc.string()
        val maxResults = binding.etMaxResults.toInt()

        // filter all checkboxes and extract the MetadataFlags's
        val flags = metadataCheckboxMap.filter { it.value.isChecked }.keys
        val metadata = EnumSet.noneOf(MetadataFlag::class.java)
        metadata.addAll(flags)

        try {
            val query = wordsBuilder {
                this.hardConstraints = hardConstraints
                this.topics = topics
                this.leftContext = leftContext
                this.rightContext = rightContext
                this.maxResults = maxResults
                this.metadata = metadata
            }
            viewModel.makeNetworkRequest(query.build())
        } catch (e: IllegalHardConstraintState) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent isWithId R.id.hard_constraint_spinner) {
            val element = constraintAdapter.constraints[position]

            binding.relatedWordsSpinner.visibility = when (element) {
                is RelatedWordsElement -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}




