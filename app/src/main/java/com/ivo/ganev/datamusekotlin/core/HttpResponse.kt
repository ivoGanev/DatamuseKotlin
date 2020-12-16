package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.core.failure.RemoteFailure

/**
 * [HttpResponse] returns either a [RemoteFailure] or [Result].
 * */
sealed class HttpResponse<out F : RemoteFailure, out R> {
    data class Failure<out F : RemoteFailure>(val failure: F) : HttpResponse<F, Nothing>()
    data class Result<out R>(val result: R) : HttpResponse<Nothing, R>()

    /**
     * Creates a Failure
     * */
    fun <F : RemoteFailure> failure(failure: F) = Failure(failure)

    /**
     * Creates a Result
     * */
    fun <R> result(result: R) = Result(result)

    val isFailure get() = this is Failure<F>
    val isResult get() = this is Result<R>

    /**
     * Applies fnF if this is a Failure and fnR if this is a Result.
     * */
    fun fold(fnF: (F) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Failure -> fnF(failure)
            is Result -> fnR(result)
        }
}

