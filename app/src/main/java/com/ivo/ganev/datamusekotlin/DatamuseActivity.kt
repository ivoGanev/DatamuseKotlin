package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.ConstraintElement.*
import com.ivo.ganev.datamusekotlin.api.format
import com.ivo.ganev.datamusekotlin.api.string
import com.ivo.ganev.datamusekotlin.api.toWordsEndpointUrl
import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.*

import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var constraintAdapter: SpinnerAdapter
    private lateinit var modelUrlBuilder: ModelUrlBuilder

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
        modelUrlBuilder = ModelUrlBuilder(binding)

        viewModel.result.observe(this, {
            var i = 1
            binding.tvResponse.text = ""

            it.forEach { wordResponse ->
                val word = wordResponse[Word::class]?.word
                val score = wordResponse[Score::class]?.score
                val definitions = wordResponse[Definitions::class]?.format()?.string()
                val tags = wordResponse[Tags::class]?.tags
                val syllablesCount = wordResponse[SyllablesCount::class]?.numSyllables
                val defHeadwords = wordResponse[DefHeadwords::class]?.defHeadword

                val jsonFormattedOutput = buildString {
                    append("$i. $word\n")
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
                i++
            }
        })

        viewModel.failure.observe(this, {
            binding.tvResponse.text = it.toString()
        })
    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            val config = modelUrlBuilder.build()
            binding.tvUrl.text = toWordsEndpointUrl(config)
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




