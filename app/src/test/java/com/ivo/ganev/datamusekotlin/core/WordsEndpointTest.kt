package com.ivo.ganev.datamusekotlin.core


import android.net.Uri
import android.os.Build
import com.ivo.ganev.datamusekotlin.core.HardConstraint.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WordsEndpointTest {
    val endpointsUrl = "https://api.datamuse.com/words?"

    @Test
    fun testIfUrlComesOutCorrect() {
        val a = wordsEndpointUrl {
            hardConstraint = MeansLike("sea side")
            topic = "meh"
            leftContext = "left"
            rightContext = "right"
            metadata = Metadata.Flag.PARTS_OF_SPEECH and Metadata.Flag.DEFINITIONS and Metadata.Flag.PRONUNCIATIONS
            maxResults = 100
        }


        println(a)
    }
}