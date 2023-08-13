package com.leg3nd.infrastructure.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.leg3nd.common.util.DateTimeUtil.toOffsetDateTime
import com.leg3nd.domain.ports.token.TokenManagerPort
import com.leg3nd.domain.ports.token.dto.TokenClaims
import org.koin.core.annotation.Single
import java.util.*
import kotlin.time.Duration

interface JwtTokenManagerConfig {
    val secret: String
    val duration: Duration
    val refreshSecret: String
    val refreshDuration: Duration
    val audience: String
}

@Single
class JwtTokenManager(
    private val jwtConfig: JwtTokenManagerConfig,
) : TokenManagerPort {
    private val accessTokenVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.secret))
        .withAudience(jwtConfig.audience)
        .build()

    private val refreshTokenVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.refreshSecret))
        .withAudience(jwtConfig.audience)
        .build()

    override fun generateAccessToken(accountId: String): String =
        JWT.create()
            .withSubject(accountId)
            .withAudience(jwtConfig.audience)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.duration.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(jwtConfig.secret))

    override fun generateRefreshToken(accountId: String): String =
        JWT.create()
            .withSubject(accountId)
            .withAudience(jwtConfig.audience)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.refreshDuration.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(jwtConfig.refreshSecret))

    override fun validateAccessToken(accessToken: String): Result<TokenClaims> = runCatching {
        val verified = accessTokenVerifier.verify(accessToken)

        TokenClaims(
            sub = verified.subject.toString(),
            aud = verified.audience.first(),
            iat = verified.issuedAt.toOffsetDateTime(),
            exp = verified.expiresAt.toOffsetDateTime(),
        )
    }

    override fun validateRefreshToken(refreshToken: String): Result<TokenClaims> = runCatching {
        val verified = refreshTokenVerifier.verify(refreshToken)

        TokenClaims(
            sub = verified.subject.toString(),
            aud = verified.audience.first(),
            iat = verified.issuedAt.toOffsetDateTime(),
            exp = verified.expiresAt.toOffsetDateTime(),
        )
    }
}
