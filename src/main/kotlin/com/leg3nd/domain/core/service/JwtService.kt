package com.leg3nd.domain.core.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.leg3nd.domain.ports.service.JwtServicePort
import org.koin.core.annotation.Single
import java.util.*
import kotlin.time.Duration

interface JwtServiceConfig {
    val secret: String
    val duration: Duration
    val refreshSecret: String
    val refreshDuration: Duration
    val audience: String
}

@Single
class JwtService(
    private val jwtConfig: JwtServiceConfig,
) : JwtServicePort {

    override val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.secret))
        .withAudience(jwtConfig.audience)
        .build()

    override val refreshVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.refreshSecret))
        .withAudience(jwtConfig.audience)
        .build()

    override fun generateToken(accountId: String): String =
        JWT.create()
            .withClaim("id", accountId)
            .withAudience(jwtConfig.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.duration.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(jwtConfig.secret))

    override fun generateRefreshToken(accountId: String): String =
        JWT.create()
            .withClaim("id", accountId)
            .withAudience(jwtConfig.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.refreshDuration.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(jwtConfig.refreshSecret))

    fun verifyRefreshToken(refreshToken: String): Result<Unit> = runCatching {
        refreshVerifier.verify(refreshToken)
        val decoded = JWT.decode(refreshToken)
        validateRefreshToken(decoded.audience).getOrThrow()
    }

    override fun validateToken(audience: List<String>): Result<Unit> = runCatching {
        if (!audience.contains(jwtConfig.audience)) throw Exception("validateToken failed, invalid audience")
    }

    override fun validateRefreshToken(audience: List<String>): Result<Unit> = runCatching {
        if (!audience.contains(jwtConfig.audience)) throw Exception("validateRefreshToken failed, invalid audience")
    }
}
