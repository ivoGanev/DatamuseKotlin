package com.ivo.ganev.datamusekotlin.configuration

import com.ivo.ganev.datamusekotlin.endpoint.words.*

data class Configuration(
    @JvmField val hardConstraint: HardConstraint? = null,
    @JvmField val topic: Topic? = null,
    @JvmField val leftContext: LeftContext? = null,
    @JvmField val rightContext: RightContext? = null,
    @JvmField val maxResults: MaxResults? = null,
    @JvmField val metadata: Metadata? = null
)