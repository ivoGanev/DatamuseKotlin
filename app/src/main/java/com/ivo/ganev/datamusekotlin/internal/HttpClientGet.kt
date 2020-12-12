package com.ivo.ganev.datamusekotlin.internal

interface HttpClientGet {
   suspend fun get(url: String) : String
}