package com.ivo.ganev.datamusekotlin.client

import com.ivo.ganev.datamusekotlin.response.QueryResponse
import com.ivo.ganev.datamusekotlin.response.RemoteFailure
import com.ivo.ganev.datamusekotlin.configuration.ConfigurationBuilder
import com.ivo.ganev.datamusekotlin.endpoint.words.WordResponse

internal interface Client {
   suspend fun query(config: ConfigurationBuilder) :
           QueryResponse<RemoteFailure, Set<WordResponse>>
}