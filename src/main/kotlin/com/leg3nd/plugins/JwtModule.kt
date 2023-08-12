package com.leg3nd.plugins

import com.leg3nd.domain.ports.service.JwtServicePort
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun Application.configureJwt() {
    val jwtService by inject<JwtServicePort>()
    // Please read the jwt property from the config file if you are using EngineMain

    install(Authentication) {
        jwt("auth-jwt") {
            authHeader { call ->
                val header = call.request.headers["Authorization"] ?: runBlocking {
                    // If Authorization header is empty, execute logic below and call.respond from here

                    call.respond("hmm")
                    return@runBlocking "empty authorization header"
                }

                try {
                    parseAuthorizationHeader(header)
                } catch (e: Exception) {
                    null
                }
            }

            verifier {
                jwtService.verifier
            }
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
//
// object PublicJwtVerifier : JWTVerifier {
//    const val publicString = "PUBLIC"
//
//    object PublicDecodedJWT : DecodedJWT {
//        override fun getIssuer(): String = publicString
//
//        override fun getSubject(): String = publicString
//
//        override fun getAudience(): MutableList<String> = mutableListOf()
//
//        override fun getExpiresAt(): Date = Date.from(Instant.now())
//
//        override fun getNotBefore(): Date = Date.from(Instant.now())
//
//        override fun getIssuedAt(): Date = Date.from(Instant.now())
//
//        override fun getId(): String = publicString
//
//        override fun getClaim(name: String?): Claim = Claim
//
//        override fun getClaims(): MutableMap<String, Claim> = publicString
//
//        override fun getAlgorithm(): String = publicString
//
//        override fun getType(): String = publicString
//
//        override fun getContentType(): String = publicString
//
//        override fun getKeyId(): String = publicString
//
//        override fun getHeaderClaim(name: String?): Claim = publicString
//
//        override fun getToken(): String = publicString
//
//        override fun getHeader(): String = publicString
//
//        override fun getPayload(): String = publicString
//
//        override fun getSignature(): String = publicString
//    }
//
//    override fun verify(token: String?): DecodedJWT {
//        println("public jwt verifier: $token")
//    }
//
//    override fun verify(jwt: DecodedJWT?): DecodedJWT {
//        println("public jwt verifier: $jwt")
//    }
// }
