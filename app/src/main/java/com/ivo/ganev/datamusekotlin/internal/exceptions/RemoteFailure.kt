package com.ivo.ganev.datamusekotlin.internal.exceptions

sealed class RemoteFailure {
    data class HttpCodeFailure(val failureCode: Int) : RemoteFailure()
}
