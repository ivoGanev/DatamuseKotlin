package com.ivo.ganev.datamusekotlin.internal.exceptions

import com.ivo.ganev.datamusekotlin.internal.HttpResponse
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf

import org.junit.Test
import kotlin.test.fail

class HttpDatamuseJsonResponseTest {
    @Test
    fun `is response a result`() {
        val response = HttpResponse.Result("Result")
        response.isResult shouldBeEqualTo true
        response.result shouldBe "Result"
    }

    @Test
    fun `is response a failure`() {
        val response =  HttpResponse.Failure(RemoteFailure.HttpCodeFailure(300))
        response.isFailure shouldBeEqualTo true
        response.failure.failureCode shouldBeEqualTo 300
        response.failure shouldBeInstanceOf RemoteFailure.HttpCodeFailure::class.java
    }

    @Test
    fun `test results folding`() {
        var response: HttpResponse<RemoteFailure, String> = HttpResponse.Failure(RemoteFailure.HttpCodeFailure(300))

        response.fold({
                      if(it is RemoteFailure.HttpCodeFailure) {
                          it.failureCode shouldBeEqualTo 300
                      }
        },{
            fail("Not supposed to trigger")
        })

        response = HttpResponse.Result("Hello world")
        response.fold({
            fail("Not supposed to trigger")
        }, {
            it shouldBe "Hello world"
        })
    }
}