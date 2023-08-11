package com.leg3nd.routes

import com.leg3nd.application.AccountController
import com.leg3nd.application.AuthController
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
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.authRoute() {
    val authController by inject<AuthController>()
    val accountController by inject<AccountController>()

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
    }

    authenticate("auth-jwt") {
        route(
            "/api/v1/auth",
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

        route("/internal/api/v1/gateway-auth", {
            tags = listOf("Gateway Auth")
            securitySchemeName = "auth-jwt"
        }) {
            get({
                description = "Gateway Auth API"
            }) {
                val principal = call.principal<JWTPrincipal>()
                val accountId = principal!!.payload.getClaim("id").asString()

                val authResponse = authController.authenticate(accountId, Account.Service.ServiceType.STUDIUM)

                authResponse.accountId?.let { call.response.headers.append("x-account-id", it) }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
