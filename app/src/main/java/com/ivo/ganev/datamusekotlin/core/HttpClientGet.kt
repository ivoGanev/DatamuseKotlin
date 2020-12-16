package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.core.failure.RemoteFailure

internal interface HttpClientGet {
   suspend fun get(url: String) : HttpResponse<RemoteFailure, Set<WordResponse>>
}