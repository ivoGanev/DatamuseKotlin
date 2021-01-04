package com.ivo.ganev.datamusekotlin.core

import com.ivo.ganev.datamusekotlin.configuration.ConfigurationBuilder
import com.ivo.ganev.datamusekotlin.configuration.ConfigurationToStringConverter

/**
 * Filter's the first result found as a value and returns
 * the entry of the map as a Pair
 * */
inline fun <K, V> Map<out K, V>.filterFirst(predicate: (V) -> Boolean): Pair<K, V>? {
    for (entry in this) {
        if (predicate(entry.value)) {
            return Pair(entry.key, entry.value)
        }
    }
    return null
}

/**
 * Will output the configuration as a URL string
 * */
fun ConfigurationBuilder.toUrl(): String = ConfigurationToStringConverter.Default.from(this.build())