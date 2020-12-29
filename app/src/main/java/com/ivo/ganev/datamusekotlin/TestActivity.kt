package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.core.HardConstraint
import com.ivo.ganev.datamusekotlin.core.HardConstraint.*
import com.ivo.ganev.datamusekotlin.core.buildWordsEndpointUrl
import com.ivo.ganev.datamusekotlin.databinding.ActivityTestBinding
import com.ivo.ganev.datamusekotlin.extenstions.isWithId
import kotlin.reflect.KClass


class TestActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    private lateinit var binding: ActivityTestBinding
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var spinner: Spinner
    private val model: TestActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hardConstraint = listOf("Means Like", "Sounds Like", "Spelled Like", "Related Words")
        spinnerAdapter = SpinnerAdapterHardConstraint(this, hardConstraint)
        spinner = binding.spinnerHardConstraint

        binding.spinnerHardConstraint.adapter = spinnerAdapter
        binding.spinnerHardConstraint.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(v: View?) {
        if(v isWithId R.id.btn_fetch) {
            val selectedItem = spinner.selectedItem as String
            model.makeNetworkRequest(buildWordsEndpointUrl {
                val word = binding.etWord.text.toString()
                hardConstraint = when(selectedItem) {
                    "Means Like" -> MeansLike(word)
                    else -> MeansLike("default")
                }
            })
            println("Fetching..")
        }
    }
}

