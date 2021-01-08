/**
 * Copyright (C) 2020 Ivo Ganev
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
package com.ivo.ganev.datamuse_kotlin.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivo.ganev.datamuse_kotlin.client.DatamuseKotlinClient
import com.ivo.ganev.datamuse_kotlin.configuration.EndpointConfiguration
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure
import com.ivo.ganev.datamuse_kotlin.response.WordResponse
import kotlinx.coroutines.launch

class DatamuseActivityViewModel : ViewModel() {
    private val client = DatamuseKotlinClient()

    /**
     * If there are any errors or failures this property will
     * notify all attached observers.
     * */
    val failure: MutableLiveData<RemoteFailure> by lazy {
        MutableLiveData<RemoteFailure>()
    }

    /**
     * As soon as the query has been returned and there are no
     * errors, this property will notify all attached observers.
     * */
    val result: MutableLiveData<Set<WordResponse>> by lazy {
        MutableLiveData<Set<WordResponse>>()
    }

    /**
     *  As soon as the query is made this will be updated with the URL
     * */
    val url: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * The function will query the Datamuse client for response.
     * It will either set the value of [failure] or [result] and update the value for [url].
     * */
    fun makeNetworkRequest(config: EndpointConfiguration) {
        viewModelScope.launch {
            val get = client.query(config)
            get.applyEither(
                { remoteFailure -> failure.postValue(remoteFailure) },
                { result.postValue(it) })
        }

        url.value = config.toUrl()
    }
}