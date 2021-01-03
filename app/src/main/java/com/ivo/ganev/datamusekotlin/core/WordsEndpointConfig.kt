package com.ivo.ganev.datamusekotlin.core

import android.net.Uri
import com.ivo.ganev.datamusekotlin.api.*
import java.util.*

internal data class WordsEndpointConfig(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
)

