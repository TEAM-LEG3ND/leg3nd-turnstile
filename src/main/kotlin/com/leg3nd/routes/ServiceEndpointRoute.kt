package com.leg3nd.routes

import com.leg3nd.application.ServiceEndpointController
import com.leg3nd.application.dto.ServiceEndpointResponse
import com.leg3nd.application.dto.UpsertServiceEndpointRequest
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.serviceEndpointRoute() {
    val serviceEndpointController by inject<ServiceEndpointController>()

    route(
        "/internal/api/v1/service-endpoint",
        {
            tags = listOf("Service Endpoint Internal API")
        },
    ) {
        post(
            {
                description = "Upsert Service Endpoint"
                request {
                    body<UpsertServiceEndpointRequest>()
                }
                response {
                    HttpStatusCode.OK to {
                        body<ServiceEndpointResponse>()
                        description = "Successful Service Endpoint Upsert"
                    }
                }
            },
        ) {
            val upsertServiceEndpointRequest = call.receive<UpsertServiceEndpointRequest>()
            val upsertServiceEndpointResponse = serviceEndpointController.upsert(upsertServiceEndpointRequest)

            call.respond(HttpStatusCode.Created)
            call.respond(upsertServiceEndpointResponse)
        }
    }
}
