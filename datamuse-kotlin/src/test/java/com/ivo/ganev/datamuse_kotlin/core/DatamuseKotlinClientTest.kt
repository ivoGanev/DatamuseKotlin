package com.ivo.ganev.datamuse_kotlin.core

import com.ivo.ganev.datamuse_kotlin.client.DatamuseKotlinClient
import com.ivo.ganev.datamuse_kotlin.configuration.WordsConfiguration
import com.ivo.ganev.datamuse_kotlin.endpoint.internal.EndpointKeyValue
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import java.io.File

class StubConfig(private val url: String) : WordsConfiguration() {
    override val path: String
        get() = TODO("Not yet implemented")

    override fun getQuery(): List<EndpointKeyValue?> {
        TODO("Not yet implemented")
    }

    override fun toUrl(): String {
        return url
    }
}


class DatamuseKotlinClientTest {
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
        val client = DatamuseKotlinClient()
        val get = client.query(StubConfig(url.toString()))

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
        val client = DatamuseKotlinClient()
        val get = client.query(StubConfig(url.toString()))

        get.isFailure shouldBeEqualTo true
        get.applyEither({ if (it is RemoteFailure.HttpCodeFailure) it.failureCode shouldBeEqualTo 400 }, {})

        server.shutdown()
    }
}