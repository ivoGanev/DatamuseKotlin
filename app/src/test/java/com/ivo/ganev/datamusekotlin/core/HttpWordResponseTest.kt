package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.api.QueryResponse
import com.ivo.ganev.datamusekotlin.api.RemoteFailure
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf

import org.junit.Test
import kotlin.test.fail

class HttpWordResponseTest {
    @Test
    fun `is response a result`() {
        val response = QueryResponse.Result("Result")
        response.isResult shouldBeEqualTo true
        response.result shouldBe "Result"
    }

    @Test
    fun `is response a failure`() {
        val response =  QueryResponse.Failure(RemoteFailure.HttpCodeFailure(300))
        response.isFailure shouldBeEqualTo true
        response.failure.failureCode shouldBeEqualTo 300
        response.failure shouldBeInstanceOf RemoteFailure.HttpCodeFailure::class.java
    }

    @Test
    fun `test results folding`() {
        var response: QueryResponse<RemoteFailure, String> = QueryResponse.Failure(
            RemoteFailure.HttpCodeFailure(
                300
            )
        )

        response.applyEither({
                      if(it is RemoteFailure.HttpCodeFailure) {
                          it.failureCode shouldBeEqualTo 300
                      }
        },{
            fail("Not supposed to trigger")
        })

        response = QueryResponse.Result("Hello world")
        response.applyEither({
            fail("Not supposed to trigger")
        }, {
            it shouldBe "Hello world"
        })
    }
}