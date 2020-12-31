package com.ivo.ganev.datamusekotlin

import androidx.test.platform.app.InstrumentationRegistry
import com.ivo.ganev.datamusekotlin.api.DatamuseClient
import com.ivo.ganev.datamusekotlin.core.*
import com.ivo.ganev.datamusekotlin.extensions.readAssetFile
import com.ivo.ganev.datamusekotlin.api.RemoteFailure
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import com.ivo.ganev.datamusekotlin.api.Metadata as ApiMetadata

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatamuseOkClientTest {
    private val client: HttpClientGet = DatamuseClient()
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

        val serverUrl = server.url("/v1/fetch/")
        val get = client.get(serverUrl.toString())

        get.isResult shouldBeEqualTo true
        val md =
            ApiMetadata(ApiMetadata.Flag.SYLLABLE_COUNT and ApiMetadata.Flag.PARTS_OF_SPEECH)
        println(md.value)
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
        val get = client.get(serverUrl.toString())

        get.isFailure shouldBeEqualTo true
        get.fold({ if(it is RemoteFailure.HttpCodeFailure) it.failureCode shouldBeEqualTo  400 }, {})

        server.shutdown()
    }
}