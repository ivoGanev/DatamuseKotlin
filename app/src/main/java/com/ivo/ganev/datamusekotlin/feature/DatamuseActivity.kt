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
package com.ivo.ganev.datamusekotlin.feature

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.feature.api.ConstraintElement.*
import com.ivo.ganev.datamusekotlin.feature.adapter.HardConstraintAdapter
import com.ivo.ganev.datamusekotlin.feature.api.UrlModel
import com.ivo.ganev.datamusekotlin.R
import com.ivo.ganev.datamusekotlin.common.buildToString
import com.ivo.ganev.datamusekotlin.common.format
import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse.Element.*
import com.ivo.ganev.datamusekotlin.common.toUrl

import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId


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
        binding.constraintSpinner.onItemSelectedListener = this
        binding.tvResponse.movementMethod = ScrollingMovementMethod()
        binding.constraintSpinner.adapter = constraintAdapter
        binding.constraintRelSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, RelatedWordsElement.codeMap.keys.toList())
        modelUrlBuilder = UrlModel(binding)

        viewModel.result.observe(this, {
            var i = 0
            binding.tvResponse.text = ""

            it.forEach { wordResponse ->
                val word = wordResponse[Word::class]?.word
                val score = wordResponse[Score::class]?.score
                val definitions = wordResponse[Definitions::class]?.format()?.buildToString()
                val tags = wordResponse[Tags::class]?.tags
                val syllablesCount = wordResponse[SyllablesCount::class]?.numSyllables
                val defHeadwords = wordResponse[DefHeadwords::class]?.defHeadword

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
                    if (defHeadwords!=null)
                        append("Def Headwords: $defHeadwords")
                    append("\n\n")
                }
                binding.tvResponse.append(jsonFormattedOutput)
            }
        })

        viewModel.failure.observe(this, {
            binding.tvResponse.text = it.toString()
        })
    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            val config = modelUrlBuilder.build()
            binding.tvUrl.text = config.toUrl()
            viewModel.makeNetworkRequest(modelUrlBuilder.build())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent isWithId R.id.constraint_spinner) {
            if (constraints[position] == RelatedWordsElement)
                binding.constraintRelSpinner.visibility = View.VISIBLE
            else
                binding.constraintRelSpinner.visibility = View.GONE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}




