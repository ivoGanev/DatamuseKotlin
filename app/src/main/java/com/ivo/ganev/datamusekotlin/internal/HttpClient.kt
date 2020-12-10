package com.ivo.ganev.datamusekotlin.internal

interface HttpClient {
   suspend fun get(url: String) : String
}