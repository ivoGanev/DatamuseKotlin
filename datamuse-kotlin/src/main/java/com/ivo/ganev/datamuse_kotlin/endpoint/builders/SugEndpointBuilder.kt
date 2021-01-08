/**
 * Copyright (C) 2020 Ivo Ganev
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
package com.ivo.ganev.datamuse_kotlin.endpoint.builders

import com.ivo.ganev.datamuse_kotlin.configuration.SugConfiguration
import com.ivo.ganev.datamuse_kotlin.endpoint.sug.Hint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.MaxResults

class SugEndpointBuilder : EndpointBuilder() {
    /**
     * Set this to set the maximum results for the query
     * @see  [MaxResults]
     * */
    var maxResults: Int? = null

    /**
    * @see [Hint]
    * */
    var hint: String? = null

    override fun build(): SugConfiguration {
        return SugConfiguration(
            hint?.let { Hint(it) },
            maxResults?.let { MaxResults(it) }
        )
    }
}

fun sugBuilder(sugBuilder: SugEndpointBuilder.() -> Unit) : SugEndpointBuilder{
    val builder = SugEndpointBuilder()
    builder.sugBuilder()
    return builder
}