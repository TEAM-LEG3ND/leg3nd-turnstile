package com.leg3nd.plugins

import com.leg3nd.application.AccountController
import com.leg3nd.application.AuthController
import com.leg3nd.application.dto.AccountResponse
import com.leg3nd.application.dto.AddServiceRequest
import com.leg3nd.application.dto.CreateAccountRequest
import com.leg3nd.application.dto.OAuthLoginRequest
import com.leg3nd.application.dto.TokenResponse
import com.leg3nd.domain.core.model.Account
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
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
        get("/ping") {
            call.respondText("pong")
        }

        route(
            "/auth",
            {
                tags = listOf("Auth")
            },
        ) {
            route("/login") {
                post(
                    "/google",
                    {
                        description = "Login with Google Authorization Code"
                        request {
                            body<OAuthLoginRequest>()
                        }
                        response {
                            HttpStatusCode.OK to {
                                description = "LEG3ND Access Token"
                                body<TokenResponse> { description = "TokenResponse" }
                                header<String>(HttpHeaders.SetCookie) {
                                    description = "Refresh {refresh_token}"
                                }
                            }
                        }
                    },
                ) {
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

        route(
            "/account",
            {
                tags = listOf("Account")
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
                    deprecated = true
                    description = "Will be moved to internal API"
                },
            ) {
                val addServiceRequestDto = call.receive<AddServiceRequest>()
                accountController.addService(addServiceRequestDto)

                call.respond(HttpStatusCode.Created)
            }
        }

        authenticate("auth-jwt") {
            route(
                "/auth",
                {
                    tags = listOf("Auth")
                    securitySchemeName = "auth-jwt"
                },
            ) {
                get(
                    "/me",
                    {
                        deprecated = true
                        description = "jwt check api, will be removed"
                    },
                ) {
                    val principal = call.principal<JWTPrincipal>()
                    val accountId = principal!!.payload.getClaim("id").asString()

                    val account = accountController.getAccountById(accountId)
                    call.respond(account)
                }
            }
        }
    }
}
