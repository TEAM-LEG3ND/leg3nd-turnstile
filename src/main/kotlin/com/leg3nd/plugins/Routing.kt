package com.leg3nd.plugins

import com.leg3nd.routes.accountRoute
import com.leg3nd.routes.authRoute
import com.leg3nd.routes.healthCheckRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoute()
        accountRoute()
        healthCheckRoute()
    }
}
