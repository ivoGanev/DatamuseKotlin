package com.ivo.ganev.datamusekotlin.internal.failure

sealed class RemoteFailure {
    data class HttpCodeFailure(val failureCode: Int) : RemoteFailure()
    data class MalformedJsonBodyFailure(val message: String?) : RemoteFailure()
}
