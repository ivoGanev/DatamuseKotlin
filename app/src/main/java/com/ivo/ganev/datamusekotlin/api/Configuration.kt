package com.ivo.ganev.datamusekotlin.api

import android.net.Uri
import com.ivo.ganev.datamusekotlin.api.*
import java.util.*

data class Configuration(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
)

