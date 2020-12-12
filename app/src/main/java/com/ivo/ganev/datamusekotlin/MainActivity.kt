package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.api.DatamuseOkHttpClientGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val client = DatamuseOkHttpClientGet()

        GlobalScope.launch {
            val get = client.get("https://api.datamuse.com/words?sp=hipopatamus")
            println(get)
        }
    }
}

