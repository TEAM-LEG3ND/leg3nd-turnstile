package com.leg3nd.domain.ports.api

import com.auth0.jwt.JWTVerifier

interface JwtServicePort {
    val verifier: JWTVerifier
    val refreshVerifier: JWTVerifier

    fun generateToken(accountId: String): String

    fun generateRefreshToken(accountId: String): String

    fun validateToken(audience: List<String>): Result<Unit>

    fun validateRefreshToken(audience: List<String>): Result<Unit>
}
