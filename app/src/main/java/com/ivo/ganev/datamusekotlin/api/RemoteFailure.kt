package com.ivo.ganev.datamusekotlin.api

sealed class RemoteFailure {
    data class HttpCodeFailure(val failureCode: Int) : RemoteFailure()
    data class MalformedJsonBodyFailure(val message: String?) : RemoteFailure()
}
