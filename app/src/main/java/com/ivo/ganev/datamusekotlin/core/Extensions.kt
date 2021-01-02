package com.ivo.ganev.datamusekotlin.core

inline fun <K, V> Map<out K, V>.filterFirst(predicate: (V) -> Boolean): Pair<K, V>? {
    for (entry in this) {
        if (predicate(entry.value)) {
            return Pair(entry.key, entry.value)
        }
    }
    return null
}