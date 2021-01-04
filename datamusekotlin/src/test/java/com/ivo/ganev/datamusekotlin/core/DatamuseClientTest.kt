package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.client.Client
import com.ivo.ganev.datamusekotlin.client.DatamuseClient
import com.ivo.ganev.datamusekotlin.client.UrlConvertible
import com.ivo.ganev.datamusekotlin.response.RemoteFailure
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import java.io.File

class UrlConvertibleStub(private val url: String) : UrlConvertible {
    override fun toUrl(): String {
        return url
    }
}

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatamuseClientTest {
    private lateinit var body: String

    @Before
    fun setUp() {
        val file = File("src\\test\\resources\\word-request-simple.json")
        body = file.readText()
    }

    @Test
    fun clientReturnsCorrectServerBodyOnSuccessfulResponseCode() = runBlocking() {
        val server = MockWebServer()
        val mockResponse = (MockResponse()
            .setBody(body)
            .setResponseCode(200))

        server.enqueue(mockResponse)
        server.start(0)

        val url = server.url("/v1/fetch/")
        val client: Client = DatamuseClient()
        val get = client.query(UrlConvertibleStub(url.toString()))

        get.isResult shouldBeEqualTo true
        server.shutdown()
    }

    @Test
    fun clientReturnsFailureWhenResponseCodeIsNotSuccessful() = runBlocking() {
        val server = MockWebServer()
        val mockResponse = (MockResponse()
            .setBody(body)
            .setResponseCode(400))

        server.enqueue(mockResponse)
        server.start(0)

        val url = server.url("/v1/fetch/")
        val client: Client = DatamuseClient()
        val get = client.query(UrlConvertibleStub(url.toString()))

        get.isFailure shouldBeEqualTo true
        get.applyEither({ if (it is RemoteFailure.HttpCodeFailure) it.failureCode shouldBeEqualTo 400 }, {})

        server.shutdown()
    }
}