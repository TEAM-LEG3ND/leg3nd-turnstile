package com.leg3nd

import com.leg3nd.plugins.configureCallLogging
import com.leg3nd.plugins.configureCors
import com.leg3nd.plugins.configureKoin
import com.leg3nd.plugins.configureRouting
import com.leg3nd.plugins.configureSerialization
import com.leg3nd.plugins.configureSwagger
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureKoin()
    configureSwagger()
    configureRouting()
    configureCors()
    configureCallLogging()
}
