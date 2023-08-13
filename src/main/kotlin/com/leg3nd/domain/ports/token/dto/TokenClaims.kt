package com.leg3nd.domain.ports.token.dto

import java.time.OffsetDateTime

data class TokenClaims(
    val sub: String,
    val aud: String,
    val iat: OffsetDateTime,
    val exp: OffsetDateTime,
)
