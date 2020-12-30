package com.ivo.ganev.datamusekotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivo.ganev.datamusekotlin.api.DatamuseClient
import com.ivo.ganev.datamusekotlin.api.RemoteFailure
import com.ivo.ganev.datamusekotlin.api.buildWordsEndpointUrl
import kotlinx.coroutines.launch

class DatamuseActivityViewModel : ViewModel() {
    private val _datamuseActivityData: MutableLiveData<DatamuseActivityView> = MutableLiveData()
    val datamuseActivityData: LiveData<DatamuseActivityView> = _datamuseActivityData

    private val _remoteFailure: MutableLiveData<RemoteFailure> = MutableLiveData()
    val remoteFailure: LiveData<RemoteFailure> = _remoteFailure

    private fun handleFailure(remoteFailure: RemoteFailure) {
        _remoteFailure.value = remoteFailure
    }

    fun handleActivityData(data: DatamuseActivityView) {
        _datamuseActivityData.value = DatamuseActivityView(data.constraint, data.word)
    }

    private val client = DatamuseClient()

    fun makeNetworkRequest(data: DatamuseActivityView) {
        viewModelScope.launch {
            val get = client.get("URL->TODO")
            get.fold({ remoteFailure ->
                when (remoteFailure) {
                    is RemoteFailure.HttpCodeFailure -> println(remoteFailure.failureCode)
                    is RemoteFailure.MalformedJsonBodyFailure -> println(remoteFailure.message)
                }
            }, {
                it.forEach { wordResponse -> println(wordResponse.elements) }
            })
        }
    }

    private fun buildUrlStringFromView(data: DatamuseActivityView): String {
        return buildWordsEndpointUrl {

        }
    }
}