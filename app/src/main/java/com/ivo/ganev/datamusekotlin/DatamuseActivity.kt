package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.UiConstraintElement.*
import com.ivo.ganev.datamusekotlin.api.HardConstraint
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code.APPROXIMATE_RHYMES

import com.ivo.ganev.datamusekotlin.databinding.DatamuseDemoActivityBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId
import com.ivo.ganev.datamusekotlin.extenstions.string


class DatamuseActivity : AppCompatActivity(),
    View.OnClickListener {
    private lateinit var binding: DatamuseDemoActivityBinding
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var apiData: ApiData

    private val viewModel: DatamuseActivityViewModel by viewModels()

    private val constraints = listOf(
        MeansLikeElement, SoundsLikeElement, SpelledLikeElement, RelatedWordsElement
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DatamuseDemoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerAdapter = SpinnerAdapterHardConstraint(this, constraints)
        binding.constraintSpinner.adapter = spinnerAdapter
        apiData = ApiData(binding)

        viewModel.result.observe(this, { it.forEach { wordResponse -> println(wordResponse.elements) } })
    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            viewModel.makeNetworkRequest(apiData.toUrlString())
        }
    }

    class ApiData(private val binding: DatamuseDemoActivityBinding) : DatamuseActivityData() {
        override fun getConstraint(): HardConstraint {
            val constraintElement = binding.constraintSpinner.selectedItem as UiConstraintElement
            val keyword = binding.etWord.string()

            return when (constraintElement) {
                is RelatedWordsElement -> constraintElement.create(APPROXIMATE_RHYMES, keyword)
                else -> constraintElement.create(keyword)
            }
        }
    }
}




