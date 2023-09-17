package com.leg3nd.routes

import com.leg3nd.api.AuthController
import com.leg3nd.api.dto.OAuthLoginRequest
import com.leg3nd.api.dto.TokenResponse
import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.ServiceType
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.authRoute() {
    val authController by inject<AuthController>()

    route(
        "/api/v1/auth",
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
        post("/refresh") {
            val refreshToken =
                call.request.cookies["Refresh"] ?: throw BadRequestException("Cookie Refresh not provided")
            val tokenPair = authController.refresh(refreshToken)

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

    route(
        "/internal/api/v1/gateway-auth",
        {
            tags = listOf("Gateway Auth")
            securitySchemeName = "auth-jwt"
            protected = true
        },
    ) {
        get() {
            call.response.headers.append("x-account-id", "userId")
            call.respond(HttpStatusCode.OK)
        }

        get(
            "/{serviceType}",
            {
                description = "Gateway Auth API"
                request {
                    pathParameter<ServiceType>("serviceType")
                    headerParameter<String>("x-endpoint")
                }
            },
        ) {
            val serviceType = runCatching {
                val serviceTypeString =
                    call.parameters["serviceType"] ?: throw Exception("x-service-type not provided")
                ServiceType.valueOf(serviceTypeString)
            }.getOrElse {
                throw BadRequestException("x-service-type parsing failed", it)
            }
            val endpoint = call.request.headers["x-endpoint"] ?: throw BadRequestException("x-endpoint not provided")
            val accessToken = call.request.parseAuthorizationHeader()?.let {
                val authHeader = it.toString()
                if (authHeader.startsWith("Bearer ")) {
                    authHeader.substring(7, authHeader.length)
                } else {
                    null
                }
            }

            val authResponse = authController.authenticate(accessToken, serviceType, endpoint)

            authResponse.accountId?.let { call.response.headers.append("x-account-id", it) }
            call.respond(HttpStatusCode.OK)
        }
    }
}
