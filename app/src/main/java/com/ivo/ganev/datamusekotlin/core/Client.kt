package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.api.QueryResponse
import com.ivo.ganev.datamusekotlin.api.RemoteFailure
import com.ivo.ganev.datamusekotlin.api.WordsEndpointConfigBuilder

internal interface Client {
   suspend fun query(config: WordsEndpointConfigBuilder) :
           QueryResponse<RemoteFailure, Set<WordResponse>>
}