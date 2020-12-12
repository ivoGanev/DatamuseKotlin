package com.ivo.ganev.datamusekotlin

import com.ivo.ganev.datamusekotlin.api.DatamuseOkHttpClientGet
import com.ivo.ganev.datamusekotlin.internal.HttpClientGet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatamuseOkClientTest {
    private val client: HttpClientGet = DatamuseOkHttpClientGet()
    val server : MockWebServer = MockWebServer()

    @ExperimentalCoroutinesApi
    @Test
    fun `fetch synonym`() = runBlockingTest() {
        // TODO: connect to the API and fetch the json string
        val get = client.get("hello")
    }
}