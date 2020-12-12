package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.api.DatamuseOkHttpClientGet
import com.ivo.ganev.datamusekotlin.internal.HttpClientGet
import com.ivo.ganev.datamusekotlin.internal.exceptions.RemoteFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatamuseOkClientTest {
    private val client: HttpClientGet = DatamuseOkHttpClientGet()
    private val body = "Test Body"

    @Test
    fun `client returns correct server body on successful response code`() = runBlocking() {
        val server = MockWebServer()
        val mockResponse = (MockResponse()
            .setBody(body)
            .setResponseCode(200))

        server.enqueue(mockResponse)
        server.start(0)

        val serverUrl = server.url("/v1/fetch/")
        val get = client.get(serverUrl.toString())

        get.isResult shouldBeEqualTo true
        get.fold({}, { it shouldBeEqualTo body })

        server.shutdown()
    }

    @Test
    fun `client returns failure when response code is not successful`() = runBlocking() {
        val server = MockWebServer()
        val mockResponse = (MockResponse()
            .setBody(body)
            .setResponseCode(400))

        server.enqueue(mockResponse)
        server.start(0)

        val serverUrl = server.url("/v1/fetch/")
        val get = client.get(serverUrl.toString())

        get.isFailure shouldBeEqualTo true
        get.fold({ if(it is RemoteFailure.HttpCodeFailure) it.failureCode shouldBeEqualTo  400 }, {})

        server.shutdown()
    }
}