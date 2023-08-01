package com.leg3nd.plugins

import com.leg3nd.domain.ports.api.JwtServicePort
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureJwt() {
    val jwtService by inject<JwtServicePort>()
    // Please read the jwt property from the config file if you are using EngineMain

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                jwtService.verifier,
            )
            validate { credential ->
                jwtService.validateToken(credential.payload.audience)
                    .getOrNull()?.let { JWTPrincipal(credential.payload) }
            }
            challenge { defaultScheme, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired, $defaultScheme")
            }
        }

        jwt("auth-jwt-refresh") {
            authHeader { call ->
                val header = call.request.cookies["Refresh"] ?: return@authHeader null

                try {
                    parseAuthorizationHeader(header)
                } catch (e: Exception) {
                    null
                }
            }
            verifier(
                jwtService.refreshVerifier,
            )
            validate { credential ->
                jwtService.validateRefreshToken(credential.payload.audience)
                    .getOrNull()?.let { JWTPrincipal(credential.payload) }
            }
            challenge { defaultScheme, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Refresh Token is not valid or has expired, $defaultScheme")
            }
        }
    }
}
