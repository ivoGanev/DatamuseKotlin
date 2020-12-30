package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.api.HttpResponse
import com.ivo.ganev.datamusekotlin.api.RemoteFailure

internal interface HttpClientGet {
   suspend fun get(url: String) : HttpResponse<RemoteFailure, Set<WordResponse>>
}