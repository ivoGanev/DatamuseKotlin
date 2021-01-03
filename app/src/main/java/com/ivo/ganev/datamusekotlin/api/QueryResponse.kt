package com.ivo.ganev.datamusekotlin.api

/**
 * [QueryResponse] returns either a [RemoteFailure] or [Result].
 * */
sealed class QueryResponse<out F : RemoteFailure, out R> {
    data class Failure<out F : RemoteFailure>(val failure: F) : QueryResponse<F, Nothing>()
    data class Result<out R>(val result: R) : QueryResponse<Nothing, R>()

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
     * Applies fnF if this is a Failure or fnR if this is a Result.
     * */
    fun applyEither(fnF: (F) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Failure -> fnF(failure)
            is Result -> fnR(result)
        }
}

