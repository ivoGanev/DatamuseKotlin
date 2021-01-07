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
import androidx.core.view.get
import com.ivo.ganev.datamuse_kotlin.R
import com.ivo.ganev.datamuse_kotlin.common.buildToString
import com.ivo.ganev.datamuse_kotlin.common.format
import com.ivo.ganev.datamuse_kotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamuse_kotlin.endpoint.words.WordResponse.Element.*
import com.ivo.ganev.datamuse_kotlin.extenstions.isWithId
import com.ivo.ganev.datamuse_kotlin.extenstions.string
import com.ivo.ganev.datamuse_kotlin.feature.adapter.HardConstraintAdapter
import com.ivo.ganev.datamuse_kotlin.feature.api.ConstraintElement.*
import com.ivo.ganev.datamuse_kotlin.feature.api.ConstraintElement.RelatedWordsElement.codeMap
import com.ivo.ganev.datamuse_kotlin.feature.api.UrlModel


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var constraintAdapter: HardConstraintAdapter
    private lateinit var modelBuilder: UrlModel

    private val viewModel: DatamuseActivityViewModel by viewModels()

    private fun configure() {
        constraintAdapter = HardConstraintAdapter(this)
        binding.btnAddConstraint.setOnClickListener(this)
        binding.hardConstraintSpinner.adapter = constraintAdapter
        binding.hardConstraintSpinner.onItemSelectedListener = this

        binding.tvResponse.movementMethod = ScrollingMovementMethod()
        binding.relatedWordsSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, codeMap.keys.toList())
        modelBuilder = UrlModel(binding)
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

    // if a query button gets clicked I need to make a network request
    // meaning:
    // 1. Collect all the data from the activity and send it to the view model.
    private fun onQuery() {
        // start by taking the hard constraint

        val hardConstraints = binding.cgHardConstraints
       // val config = modelBuilder.build()
     //   binding.tvUrl.text = config.buildUrl()
      //  viewModel.makeNetworkRequest(modelBuilder.build())
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




