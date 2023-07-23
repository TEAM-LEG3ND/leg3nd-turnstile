package com.leg3nd.plugins

import com.leg3nd.application.AccountController
import com.leg3nd.application.dto.AddServiceRequest
import com.leg3nd.application.dto.CreateAccountRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val accountController by inject<AccountController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/account") {
            post {
                val createAccountRequestDto = call.receive<CreateAccountRequest>()
                val createdAccountId = accountController.create(createAccountRequestDto)

                call.respondText(createdAccountId)
                call.respond(HttpStatusCode.Created)
            }

            post("/service") {
                val addServiceRequestDto = call.receive<AddServiceRequest>()
                accountController.addService(addServiceRequestDto)

                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
