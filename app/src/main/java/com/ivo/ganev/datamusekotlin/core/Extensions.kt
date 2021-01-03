package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.api.ConfigurationBuilder
import com.ivo.ganev.datamusekotlin.api.ConfigurationToStringConverter

inline fun <K, V> Map<out K, V>.filterFirst(predicate: (V) -> Boolean): Pair<K, V>? {
    for (entry in this) {
        if (predicate(entry.value)) {
            return Pair(entry.key, entry.value)
        }
    }
    return null
}

fun ConfigurationBuilder.toUrl(): String = ConfigurationToStringConverter.Default.from(this.build())