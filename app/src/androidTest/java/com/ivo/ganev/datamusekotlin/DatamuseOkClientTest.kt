package com.ivo.ganev.datamusekotlin

import androidx.test.platform.app.InstrumentationRegistry
import com.ivo.ganev.datamusekotlin.api.*
import com.ivo.ganev.datamusekotlin.core.*
import com.ivo.ganev.datamusekotlin.extensions.readAssetFile
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatamuseOkClientTest {
    private val client: Client = DatamuseClient()
    private lateinit var  body: String

    @Before
    fun setUp() {
        body = InstrumentationRegistry.getInstrumentation().targetContext.assets.readAssetFile("word-request-simple.json")
    }

    @Test
    fun clientReturnsCorrectServerBodyOnSuccessfulResponseCode() = runBlocking() {
        val server = MockWebServer()
        val mockResponse = (MockResponse()
            .setBody(body)
            .setResponseCode(200))

        server.enqueue(mockResponse)
        server.start(0)

        //val serverUrl = server.url("/v1/fetch/")
        val get = client.query(buildWordsEndpointUrl {
            hardConstraint = HardConstraint.MeansLike("hello")
        })

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

        val serverUrl = server.url("/v1/fetch/")
        val get = client.query(buildWordsEndpointUrl {
            hardConstraint = HardConstraint.MeansLike("hello")
        })

        get.isFailure shouldBeEqualTo true
        get.applyEither({ if(it is RemoteFailure.HttpCodeFailure) it.failureCode shouldBeEqualTo  400 }, {})

        server.shutdown()
    }

}