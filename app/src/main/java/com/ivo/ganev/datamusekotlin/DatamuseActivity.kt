package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.UiConstraintElement.*
import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.*

import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener {
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var datamuseBindingBuilderBinder: DemoModelToUrlBuilderBuilderBinder

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
        datamuseBindingBuilderBinder = DemoModelToUrlBuilderBuilderBinder(binding)

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

                binding.tvResponse.append("$i. $word , Score: $score, Definitions: $definitions, " +
                        "Tags: $tags, Syllable Count: $syllablesCount, Def Headwords: $defHeadwords\n")
                i++
            } })

        viewModel.failure.observe(this, {
            binding.tvResponse.text = it.toString()
        })
    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            viewModel.makeNetworkRequest(datamuseBindingBuilderBinder.toUrlString())
        }
    }

}




