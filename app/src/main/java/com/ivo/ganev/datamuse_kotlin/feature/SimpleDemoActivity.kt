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
package com.ivo.ganev.datamuse_kotlin.feature

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ivo.ganev.datamuse_kotlin.R
import com.ivo.ganev.datamuse_kotlin.client.DatamuseClient
import com.ivo.ganev.datamuse_kotlin.common.buildToString
import com.ivo.ganev.datamuse_kotlin.common.format
import com.ivo.ganev.datamuse_kotlin.configuration.buildWordsEndpointUrl
import com.ivo.ganev.datamuse_kotlin.endpoint.words.*
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Simple activity to demonstrate how datamuse-kotlin works.
 * */
class SimpleDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.datamuse_demo_activity_simple)
        val queryTextView = findViewById<TextView>(R.id.tv_simple_query)

        /*
      * Use the buildWordsEndpointUrl builder function to create
      * an endpoints configuration. In this example, you build a
      * query to look for: A word which has a meaning related to
      * "elephant" where the left context is "big", maximum
      * number of results will be ten, and also will display the
      * definitions of each of those words.
      * */
        val query1 = buildWordsEndpointUrl {
            hardConstraints = hardConstraintsOf(HardConstraint.MeansLike("elephant"))
            leftContext = "big"
            maxResults = 10
            metadata = flagsOf(MetadataFlag.WORD_FREQUENCY)
        }

        // https://api.datamuse.com/words?ml=elephant&lc=big&max=10&md=d
        println(query1.buildUrl())

        /*
         * Another configuration which won't return anything because the query is
         * complex and meaningless. Never the less it demonstrates how to assign
         *  all the properties to create a query
         * */
        val query2 = buildWordsEndpointUrl {
            // you can chain constraints with the help of "and" infix function
            hardConstraints = HardConstraint.RelatedWords(HardConstraint.RelatedWords.Code.ANTONYMS, "duck") and
                    HardConstraint.SpelledLike("b*")
            topics = "sweet, little" // maximum 5 topics are allowed
            leftContext = "left context"
            rightContext = "right context"
            maxResults = 100
            // and again you can chain metadata flags with the help of "and" infix function
            metadata = MetadataFlag.DEFINITIONS and MetadataFlag.PRONUNCIATIONS and MetadataFlag.WORD_FREQUENCY
        }

        // https://api.datamuse.com/words?rel_ant=duck&sp=b*&topics=sweet%2C%20little&lc=left%20context&rc=right%20context&max=100&md=drf
        println(query2.buildUrl())

        val datamuseClient = DatamuseClient()

        queryTextView.text = ""
        // The query is suspendable function so you should launch it from a coroutine
        GlobalScope.launch(Dispatchers.Main) {
            // await for the result
            val query = datamuseClient.queryWordsEndpointAsync(query1).await()

            query.applyEither({
                // Will trigger only when there is some kind of a failure, usually a bad response code
                    remoteFailure ->
                when (remoteFailure) {
                    is RemoteFailure.HttpCodeFailure -> println(remoteFailure.failureCode) // Failed Http Response codes?
                    is RemoteFailure.MalformedJsonBodyFailure -> println(remoteFailure.message) // Any serialization error
                }
            }, {
                // This part of the function will be applied when a successful query has been made

                // wordResponses: is the collection of words and their properties returned from the query, for example,
                // in query1 we've build a query to look for words with a similar meaning of an
                // "elephant" where the left context was "big", meaning we will be looking for words
                // that are closely related to "big elephant". Here a part of the response in JSON would look like:
                // {"word":"jumbo","score":57932,"tags":["adj","f:1.176501"}
                //  where wordResponses as a Set<WordResponse> will contain:
                //  WordResponse.Element.Word, WordResponse.Element.Score, WordResponse.Element.Tags(the API will return
                //  this even if you didn't specified it explicitly). The rest of the elements will be discarded.
                wordResponses ->
                wordResponses.forEach {
                    for (element in it.elements) {
                        when (element) {
                            is WordResponse.Element.Word -> queryTextView.append("\nWord: ${element.word}")
                            is WordResponse.Element.Score ->  queryTextView.append("\n Score: ${element.score}")
                            is WordResponse.Element.Definitions ->  queryTextView.append("\n Definitions: ${element.defs}")
                            is WordResponse.Element.Tags ->  queryTextView.append("\n Tags: ${element.tags}")
                            is WordResponse.Element.SyllablesCount ->  queryTextView.append("\n Syllable Count: ${element.numSyllables}")
                            is WordResponse.Element.DefHeadwords ->  queryTextView.append("\n DefHeadwords ${element.defHeadword}")
                        }
                    }
                }
            })
        }
    }
}