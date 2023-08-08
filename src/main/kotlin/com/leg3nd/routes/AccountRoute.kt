package com.leg3nd.routes

import com.leg3nd.application.AccountController
import com.leg3nd.application.dto.AccountResponse
import com.leg3nd.application.dto.AddServiceRequest
import com.leg3nd.application.dto.CreateAccountRequest
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.accountRoute() {
    val accountController by inject<AccountController>()

    // TODO: add basic auth to internal api for dev purpose
    route(
        "/internal/api/v1/account",
        {
            tags = listOf("Account Internal API")
        },
    ) {
        get(
            "/{accountId}",
            {
                description = "Get Account By Id"
                request {
                    pathParameter<String>("accountId")
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Successful Account Response"
                        body<AccountResponse> { description = "AccountResponse" }
                    }
                }
            },
        ) {
            val accountId = call.parameters["accountId"] ?: throw BadRequestException("No account id provided")

            val account = accountController.getAccountById(accountId)
            call.respond(account)
        }

        post(
            {
                deprecated = true
            },
        ) {
            val createAccountRequestDto = call.receive<CreateAccountRequest>()
            val createdAccountId = accountController.create(createAccountRequestDto)

            call.respondText(createdAccountId)
            call.respond(HttpStatusCode.Created)
        }

        post(
            "/service",
            {
                description = "Sign up for LEG3ND dedicated service"
                request {
                    body<AddServiceRequest>()
                }
                response {
                    HttpStatusCode.Created to {
                        description = "Successful Service Sign Up"
                    }
                }
            },
        ) {
            val addServiceRequestDto = call.receive<AddServiceRequest>()
            accountController.addService(addServiceRequestDto)

            call.respond(HttpStatusCode.Created)
        }
    }
}
