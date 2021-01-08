package com.ivo.ganev.datamuse_kotlin.endpoint.builders

import com.ivo.ganev.datamuse_kotlin.configuration.EndpointConfiguration

abstract class EndpointBuilder
{
    abstract fun build() : EndpointConfiguration
}