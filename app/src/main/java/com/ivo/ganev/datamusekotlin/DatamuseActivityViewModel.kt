package com.ivo.ganev.datamusekotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivo.ganev.datamusekotlin.api.DatamuseClient
import com.ivo.ganev.datamusekotlin.api.HttpResponse
import com.ivo.ganev.datamusekotlin.api.RemoteFailure
import com.ivo.ganev.datamusekotlin.api.buildWordsEndpointUrl
import com.ivo.ganev.datamusekotlin.core.WordResponse
import kotlinx.coroutines.launch

class DatamuseActivityViewModel : ViewModel() {
    private val client = DatamuseClient()

    val failure: MutableLiveData<RemoteFailure> by lazy {
        MutableLiveData<RemoteFailure>()
    }

    val result: MutableLiveData<Set<WordResponse>> by lazy {
        MutableLiveData<Set<WordResponse>>()
    }

    fun makeNetworkRequest(url: String) {
        viewModelScope.launch {
            val get = client.get(url)
            get.fold(
                { remoteFailure -> failure.postValue(remoteFailure) },
                { result.postValue(it) })
        }
    }

}