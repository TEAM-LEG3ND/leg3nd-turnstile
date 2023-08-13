package com.leg3nd.domain.ports.token

import com.leg3nd.domain.ports.token.dto.TokenClaims

interface TokenManagerPort {
    fun generateAccessToken(accountId: String): String

    fun generateRefreshToken(accountId: String): String

    fun validateAccessToken(accessToken: String): Result<TokenClaims>

    fun validateRefreshToken(refreshToken: String): Result<TokenClaims>
}
