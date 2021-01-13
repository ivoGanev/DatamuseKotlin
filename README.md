# Datamuse-Kotlin
Datamuse-Kotlin is a library for calling the Datamuse REST API.

Check out https://www.datamuse.com/api/ for detailed API information.

# Releases
This library is available at JCenter.

```
implementation "com.ivo.ganev:datamuse-kotlin:1.0.1"
```
# What is Datamuse?
```
The Datamuse API is a word-finding query engine for developers.
You can use it in your apps to find words that match a given set 
of constraints and that are likely in a given context.
You can specify a wide variety of constraints on meaning, 
spelling, sound, and vocabulary in your queries, in any combination.
```

# What is it good for?
```
Applications use the API for a wide range of features, including autocomplete
on text input fields, search relevancy ranking, assistive writing apps, 
word games, and more.
```
 
## Create a Datamuse query

The most easy way to create a query is using the endpoint builder function.

```kotlin
 /*
* Use the words endpoint builder function to create
* an endpoints configuration. In this example, you will build a
* query to look for: A word which has a meaning related to
* "elephant" where the left context is "big", maximum
* number of results will be ten, with definitions included.
* */
val query1 = wordsBuilder {
    hardConstraints = hardConstraintsOf(HardConstraint.MeansLike("elephant"))
    leftContext = "big"
    maxResults = 10
    metadata = flagsOf(MetadataFlag.DEFINITIONS)
}

// https://api.datamuse.com/words?ml=elephant&lc=big&max=10&md=d
println(query1.build().toUrl())
   ```
   
## Yet another query
This time let's create a little more complex example.

```kotlin

/*
 * Another configuration. For demonstration purposes we will
 * create a query with all the builder properties assigned, but
 * be aware that this query won't be able to return anything meaningful.
 * */
val query2 = wordsBuilder {
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
println(query2.build().toUrl())

   ```
        
## Setup a Simple Datamuse Client

After creating a Datamuse query you would need a client to use it.

```kotlin

// ..continuing from the code above
val datamuseClient = DatamuseClient()

```

## Setup a Datamuse Client in a ViewModel

To decouple your app you might want to put the client inside a ViewModel

```kotlin

class DatamuseActivityViewModel : ViewModel() {
    private val client = DatamuseKotlinClient()

    /**
     * If there are any errors or failures this property will
     * notify all attached observers.
     * */
    val failure: MutableLiveData<RemoteFailure> by lazy {
        MutableLiveData<RemoteFailure>()
    }

    /**
     * As soon as the query has been returned and there are no
     * errors, this property will notify all attached observers.
     * */
    val result: MutableLiveData<Set<WordResponse>> by lazy {
        MutableLiveData<Set<WordResponse>>()
    }

    /**
     *  As soon as the query is made this will be updated with the URL
     * */
    val url: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * The function will query the Datamuse client for response.
     * It will either set the value of [failure] or [result] and update the value for [url].
     * */
    fun makeNetworkRequest(config: EndpointConfiguration) {
        viewModelScope.launch {
            val get = client.query(config)
            get.applyEither(
                { remoteFailure -> failure.postValue(remoteFailure) },
                { result.postValue(it) })
        }

        url.value = config.toUrl()
    }
}
```

## Without a ViewModel
This time we will use the async query to await for the result.

```kotlin

// The query is suspendable function so you should launch it from a coroutine
lifecycleScope.launch(Dispatchers.Main) {
    // await for the result
    val wordsQuery = datamuseClient.query(query1.build())
    //..
 ```

## Working with the query
Continuing from the previous code:
 ```kotlin
   //..
  wordsQuery.applyEither({
				// ..
                // Will trigger only when there is some kind of a failure, usually a bad response code.
                    remoteFailure ->
                when (remoteFailure) {
                    is RemoteFailure.HttpCodeFailure -> println(remoteFailure.failureCode) // Failed Http Response codes?
                    is RemoteFailure.MalformedJsonBodyFailure -> println(remoteFailure.message) // Any serialization error.
                }
            }, {
                // This part of the function will be applied when a successful query has been made.

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
                            is WordResponse.Element.Score -> queryTextView.append("\n Score: ${element.score}")
                            // you can use format() to separate the part of word from the definition because the response comes as:
                            // "defs":["adj\tof great mass; huge and bulky"]}
                            is WordResponse.Element.Definitions -> queryTextView.append(
                                "\n Definitions: ${
                                    element.format().string()
                                }"
                            )
                            is WordResponse.Element.Tags -> queryTextView.append("\n Tags: ${element.tags}")
                            is WordResponse.Element.SyllablesCount -> queryTextView.append("\n Syllable Count: ${element.numSyllables}")
                            is WordResponse.Element.DefHeadwords -> queryTextView.append("\n DefHeadwords ${element.defHeadword}")
                        }
                    }
                }
            })
        }

```

# Auto complete endpoint
From https://www.datamuse.com/api/ :
```
This resource is useful as a backend for “autocomplete” widgets
on websites and apps when the vocabulary of possible search terms is very large.
```
Here is how to use it:

```kotlin
   
lifecycleScope.launch(Dispatchers.Main) {
    // making a query for the autocomplete endpoint is easy:
    val autoComplete = sugBuilder {
        hint = "swee"
        maxResults = 10
    }
    // await for the result
    val sugQuery = datamuseClient.query(autoComplete.build())
    if (sugQuery.isResult)
        sugQuery.applyEither({}, { sugResponse ->
            // iterate through all the word results
            sugResponse.forEach {
                // This is another way to get an element from the query.
                // You can shorten WordResponse.Element.Word to just Word
                // by adding it as an import.
                val wordElement = it[WordResponse.Element.Word::class]

                //sweep,sweet,sweeping,sweetheart..
                println(wordElement?.word)
            }
        })
}

```
# Check Out the Demo App
Make sure to check the [demo app](/app)
to see a complete project on how to use the library

# License
```
Copyright (C) 2020 Ivo Ganev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
