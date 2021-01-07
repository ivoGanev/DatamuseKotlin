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
 *//*

package com.ivo.ganev.datamuse_kotlin.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamuse_kotlin.R
import com.ivo.ganev.datamuse_kotlin.client.DatamuseClient
import com.ivo.ganev.datamuse_kotlin.common.buildToString
import com.ivo.ganev.datamuse_kotlin.common.format
import com.ivo.ganev.datamuse_kotlin.configuration.buildWordsEndpointUrl
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint
import com.ivo.ganev.datamuse_kotlin.endpoint.words.Metadata
import com.ivo.ganev.datamuse_kotlin.endpoint.words.WordResponse
import com.ivo.ganev.datamuse_kotlin.endpoint.words.and
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_activity)
        */
/*
      * Use the buildWordsEndpointUrl builder function to create
      * an endpoints configuration. In this example, you build a
      * query to look for: A word which has a meaning related to
      * "elephant" where the left context is "big", maximum
      * number of results will be ten, and also will display the
      * definitions of each of those words.
      * *//*

        val config1 = buildWordsEndpointUrl {
            hardConstraint = HardConstraint.MeansLike("elephant")
            leftContext = "big"
            maxResults = 10
            metadata = Metadata.flags(MetadataFlag.DEFINITIONS)
        }

        // https://api.datamuse.com/words?ml=elephant&lc=big&max=10&md=d
        println(config1.buildUrl())

        */
/*
         * Another configuration
         * *//*

        val config2 = buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(HardConstraint.RelatedWords.Code.ANTONYMS, "donkey")
            topics = "sweet, little" // maximum 5 topics are allowed
            leftContext = "left context"
            rightContext = "right context"
            maxResults = 100
            metadata = MetadataFlag.DEFINITIONS and MetadataFlag.PRONUNCIATIONS and MetadataFlag.WORD_FREQUENCY
        }

        // https://api.datamuse.com/words?rel_ant=donkey&topics=sweet%2C%20little&lc=left%20context&rc=right%20context&max=100&md=drf
        println(config2.buildUrl())

        val datamuseClient = DatamuseClient()
        // The query is suspendable function so you should launch it from a coroutine
        val query = datamuseClient.query(config2)

        query.applyEither({
            // Will trigger only when there is some kind of a failure, usually a bad response code
                          remoteFailure ->
            when(remoteFailure) {
                is RemoteFailure.HttpCodeFailure -> println(remoteFailure.failureCode) // Failed Http Response codes?
                is RemoteFailure.MalformedJsonBodyFailure -> println(remoteFailure.message) // Any serialization error
            }
        }, {
            // This function will be applied when a successful query has been made

            // wordResponse returns a Set<WordResponse> from which you can get
            // any element
                wordResponse ->
            wordResponse.forEach {
                for(element in it.elements) {
                    when(element){
                        is WordResponse.Element.Word -> TODO()
                        is WordResponse.Element.Score -> TODO()
                        is WordResponse.Element.Definitions -> TODO()
                        is WordResponse.Element.Tags -> TODO()
                        is WordResponse.Element.SyllablesCount -> TODO()
                        is WordResponse.Element.DefHeadwords -> TODO()
                    }

                }
                val word = it[WordResponse.Element.Word::class]?.word
                val score = it[WordResponse.Element.Score::class]?.score
                val definitions = it[WordResponse.Element.Definitions::class]?.format()?.buildToString()
                val tags = it[WordResponse.Element.Tags::class]?.tags
                val syllablesCount = it[WordResponse.Element.SyllablesCount::class]?.numSyllables
                val defHeadwords = it[WordResponse.Element.DefHeadwords::class]?.defHeadword
        })

    }
}*/
