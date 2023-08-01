package com.leg3nd.plugins

import com.leg3nd.application.AccountController
import com.leg3nd.application.AuthController
import com.leg3nd.application.dto.AddServiceRequest
import com.leg3nd.application.dto.CreateAccountRequest
import com.leg3nd.application.dto.OAuthLoginRequest
import com.leg3nd.application.dto.TokenResponse
import com.leg3nd.domain.core.model.Account
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authController by inject<AuthController>()
    val accountController by inject<AccountController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/auth") {
            route("/login") {
                post("/google") {
                    val oAuthLoginRequest = call.receive<OAuthLoginRequest>()
                    val tokenPair = authController.login(Account.OAuthProvider.GOOGLE, oAuthLoginRequest)

                    call.response.cookies.append(
                        // TODO: add more cookie setting for example secure and httpOnly
                        Cookie(
                            name = "Refresh",
                            value = tokenPair.refreshToken,
                        ),
                    )
                    call.respond(TokenResponse.fromTokenPair(tokenPair))
                }
            }
        }

        route("/account") {
            get("/{accountId}") {
                val accountId = call.parameters["accountId"] ?: throw BadRequestException("No account id provided")

                val account = accountController.getAccountById(accountId)
                call.respond(account)
            }

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

        authenticate("auth-jwt") {
            route("/auth") {
                get("/me") {
                    val principal = call.principal<JWTPrincipal>()
                    val accountId = principal!!.payload.getClaim("id").asString()

                    val account = accountController.getAccountById(accountId)
                    call.respond(account)
                }
            }
        }
    }
}
