# DatamuseKotlin
A Datamuse API library
   
## Create a Datamuse query

```kotlin
 class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_activity)
        
        /*
      * Use the buildWordsEndpointUrl builder function to create
      * an endpoints configuration. In this example, you build a
      * query to look for: A word which has a meaning related to
      * "elephant" where the left context is "big", maximum
      * number of results will be ten, and also will display the
      * definitions of each of those words.
      * */
        val config1 = buildWordsEndpointUrl {
            hardConstraint = HardConstraint.MeansLike("elephant")
            leftContext = "big"
            maxResults = 10
            metadata = Metadata.flags(MetadataFlag.DEFINITIONS)
        }

        // https://api.datamuse.com/words?ml=elephant&lc=big&max=10&md=d
        println(config1.buildUrl())

        /*
         * Another configuration
         * */
        val config2 = buildWordsEndpointUrl {
            hardConstraint = HardConstraint.RelatedWords(HardConstraint.RelatedWords.Code.ANTONYMS, "donkey")
            topics = "sweet, little" // maximum 5 topics are allowed
            leftContext = "left context"
            rightContext = "right context"
            maxResults = 100
            // note that when there are more than one metadata flags, you can use "and" to pack them together.
            metadata = MetadataFlag.DEFINITIONS and MetadataFlag.PRONUNCIATIONS and MetadataFlag.WORD_FREQUENCY
        }

        // https://api.datamuse.com/words?rel_ant=donkey&topics=sweet%2C%20little&lc=left%20context&rc=right%20context&max=100&md=drf
        println(config2.buildUrl())
    }
}
   ```
        
## Setup a Simple Datamuse Client

After creating a Datamuse query you need a client to use it

```kotlin

        // ..continuing from the code above
        
        val datamuseClient = DatamuseClient()
        // The query is suspendable function so you should launch it from a coroutine
        val query = datamuseClient.query(config1)

```

## Setup a Datamuse Client in a ViewModel

To decouple your app you might want to put the client inside a ViewModel

```kotlin

class DatamuseActivityViewModel : ViewModel() {
    private val client = DatamuseClient()

    val failure: MutableLiveData<RemoteFailure> by lazy {
        MutableLiveData<RemoteFailure>()
    }

    val result: MutableLiveData<Set<WordResponse>> by lazy {
        MutableLiveData<Set<WordResponse>>()
    }

    fun makeNetworkRequest(config: EndpointBuilder<UrlConfig>) {
        viewModelScope.launch {
            val get = client.query(config)
            get.applyEither(
                { remoteFailure -> failure.postValue(remoteFailure) },
                { result.postValue(it) })
        }
    }
}

```


    
