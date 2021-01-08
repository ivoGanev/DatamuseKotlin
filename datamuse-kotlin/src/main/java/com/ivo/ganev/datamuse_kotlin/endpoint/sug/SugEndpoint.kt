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
package com.ivo.ganev.datamuse_kotlin.endpoint.sug

import com.ivo.ganev.datamuse_kotlin.endpoint.internal.EndpointKeyValue

/**
 * Prefix hint string; typically, the characters that the user has entered
 * so far into a search box. (Note: The results are sorted by a measure of popularity.
 * The results may include spell-corrections of the prefix hint or semantically
 * similar terms when exact matches cannot be found; that is to say, the prefix
 * hint will not necessarily form a prefix of each result.)
 * */
class Hint(override val value: String) : EndpointKeyValue {
    override val key: String = "s"
}