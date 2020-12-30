package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.UiConstraintElement.*
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code.APPROXIMATE_RHYMES

import com.ivo.ganev.datamusekotlin.databinding.ActivityTestBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId


class DatamuseActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private lateinit var binding: ActivityTestBinding
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var spinner: Spinner
    private val viewModelDatamuse: DatamuseActivityViewModel by viewModels()

    private val spinnerConstraintList = listOf(
        MeansLikeElement, SoundsLikeElement, SpelledLikeElement, RelatedWordsElement
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val constraintNames = spinnerConstraintList.map { it.label }
        spinnerAdapter = SpinnerAdapterHardConstraint(this, constraintNames)
        spinner = binding.spinnerHardConstraint

        binding.spinnerHardConstraint.adapter = spinnerAdapter
        binding.spinnerHardConstraint.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val constraintInUISpinner = spinnerConstraintList[position]
        val keyword = binding.etWord.text.toString()

        val constraint = when (constraintInUISpinner) {
            is RelatedWordsElement -> constraintInUISpinner.create(APPROXIMATE_RHYMES, keyword)
            else -> constraintInUISpinner.create(keyword)
        }
        println(constraint.toString() + "::"  + constraint.key + "::" + constraint.value)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(v: View?) {
        if (v isWithId R.id.btn_fetch) {
            //   viewModelDatamuse.makeNetworkRequest()
            println("Fetching..")
        }
    }

    private fun getViewData(): DatamuseActivityView {
        return DatamuseActivityView(
            binding.spinnerHardConstraint.selectedItem as String,
            binding.etWord.text.toString()
        )
    }

}




