package com.ivo.ganev.datamusekotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivo.ganev.datamusekotlin.api.DatamuseClient
import com.ivo.ganev.datamusekotlin.core.*
import com.ivo.ganev.datamusekotlin.core.failure.RemoteFailure
import kotlinx.coroutines.launch

class TestActivityViewModel : ViewModel() {
    private val client = DatamuseClient()

    fun makeNetworkRequest(url: String) {
        viewModelScope.launch {
            val get = client.get(url)
            get.fold({ remoteFailure ->
                when (remoteFailure) {
                    is RemoteFailure.HttpCodeFailure -> println(remoteFailure.failureCode)
                    is RemoteFailure.MalformedJsonBodyFailure -> println(remoteFailure.message)
                }
            }, {
                it.forEach {wordResponse -> println(wordResponse.elements) }
            })
        }
    }




}