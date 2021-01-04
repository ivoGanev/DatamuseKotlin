/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
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
package com.ivo.ganev.datamusekotlin.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivo.ganev.datamusekotlin.client.DatamuseClient
import com.ivo.ganev.datamusekotlin.configuration.ConfigurationBuilder
import com.ivo.ganev.datamusekotlin.response.RemoteFailure
import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse
import kotlinx.coroutines.launch

class DatamuseActivityViewModel : ViewModel() {
    private val client = DatamuseClient()

    val failure: MutableLiveData<RemoteFailure> by lazy {
        MutableLiveData<RemoteFailure>()
    }

    val result: MutableLiveData<Set<WordResponse>> by lazy {
        MutableLiveData<Set<WordResponse>>()
    }

    fun makeNetworkRequest(config: ConfigurationBuilder) {
        viewModelScope.launch {
            val get = client.query(config)
            get.applyEither(
                { remoteFailure -> failure.postValue(remoteFailure) },
                { result.postValue(it) })
        }
    }
}