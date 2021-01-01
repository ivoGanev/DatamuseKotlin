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
import com.ivo.ganev.datamusekotlin.core.WordResponse.Element.*

import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var constraintAdapter: SpinnerAdapter
    private lateinit var datamuseBindingFuseWordsEndpoints: DemoModelWordsEndpointsUrlBinder

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
        datamuseBindingFuseWordsEndpoints = DemoModelWordsEndpointsUrlBinder(binding)

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

                binding.tvResponse.append("$i. $word\n Score: $score\n Definitions: $definitions\n " +
                        "Tags: $tags\n Syllable Count: $syllablesCount\n Def Headwords: $defHeadwords\n\n")
                i++
            } })

        viewModel.failure.observe(this, {
            binding.tvResponse.text = it.toString()
        })
    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            val url = datamuseBindingFuseWordsEndpoints.toUrlString()
            binding.tvUrl.text = url
            viewModel.makeNetworkRequest(url)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent isWithId R.id.constraint_spinner) {
            if(constraints[position] == RelatedWordsElement)
                binding.constraintRelSpinner.visibility = View.VISIBLE
            else
                binding.constraintRelSpinner.visibility = View.GONE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}




