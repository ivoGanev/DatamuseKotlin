package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.api.DatamuseClient
import com.ivo.ganev.datamusekotlin.core.failure.RemoteFailure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val client = DatamuseClient()

        GlobalScope.launch {
            val get = client.get("https://api.datamuse.com/words?")
            get.fold({ remoteFailure ->
                if (remoteFailure is RemoteFailure.HttpCodeFailure)
                    println(remoteFailure.failureCode)
            }, {
                println(it.size)
            })
        }
    }
}

