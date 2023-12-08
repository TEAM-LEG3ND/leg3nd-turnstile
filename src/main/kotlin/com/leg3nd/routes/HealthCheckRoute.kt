package com.leg3nd.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.healthCheckRoute() {
    route("/api/v1/ping") {
        get {
            call.respondText("pong~!")
        }
    }
}
