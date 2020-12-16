package com.ivo.ganev.datamusekotlin.internal

import com.ivo.ganev.datamusekotlin.internal.failure.RemoteFailure

internal interface HttpClientGet {
   suspend fun get(url: String) : HttpResponse<RemoteFailure, Set<WordResponse>>
}