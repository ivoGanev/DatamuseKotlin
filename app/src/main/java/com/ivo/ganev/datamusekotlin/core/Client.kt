package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.api.QueryResponse
import com.ivo.ganev.datamusekotlin.api.RemoteFailure
import com.ivo.ganev.datamusekotlin.api.ConfigurationBuilder

internal interface Client {
   suspend fun query(config: ConfigurationBuilder) :
           QueryResponse<RemoteFailure, Set<WordResponse>>
}