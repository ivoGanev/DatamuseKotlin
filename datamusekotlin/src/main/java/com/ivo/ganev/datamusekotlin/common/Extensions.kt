/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivo.ganev.datamusekotlin.common

import com.ivo.ganev.datamusekotlin.configuration.WordsEndpointBuilder
import com.ivo.ganev.datamusekotlin.configuration.ConfigurationConverter

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