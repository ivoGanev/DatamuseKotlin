package com.ivo.ganev.datamusekotlin.extensions

import android.content.res.AssetManager
import androidx.test.platform.app.InstrumentationRegistry
import java.io.BufferedReader
import java.io.InputStreamReader


fun AssetManager.readAssetFile(path: String): String {
    val file = this.open(path)
    val reader = BufferedReader(InputStreamReader(file))
    reader.use {
        return it.readText()
    }
}
