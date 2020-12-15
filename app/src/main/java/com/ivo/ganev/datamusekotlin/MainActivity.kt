package com.ivo.ganev.datamusekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamusekotlin.api.DatamuseOkHttpClientGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val client = DatamuseOkHttpClientGet()

        GlobalScope.launch {
            val get = client.get("https://api.datamuse.com/words?sp=hipopatamus")
            get.fold({}, {
              //  val o = Json.parseToJsonElement(it)
                //val o = "{\"word\":\"hippopotamus\",\"score\":501,\"defs\":[\"n\\tmassive thick-skinned herbivorous animal living in or around rivers of tropical Africa\"]},{\"word\":\"hippotamus\",\"score\":1}"
                //val decodeFromJsonElement = Json.decodeFromString<WordRequest>(o)

               // println(decodeFromJsonElement)
            })
        }
    }
}

