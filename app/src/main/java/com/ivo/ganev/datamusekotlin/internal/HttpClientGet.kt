package com.ivo.ganev.datamusekotlin.internal

import com.ivo.ganev.datamusekotlin.internal.exceptions.RemoteFailure

interface HttpClientGet {
   suspend fun get(url: String) : HttpResponse<RemoteFailure, String>
}