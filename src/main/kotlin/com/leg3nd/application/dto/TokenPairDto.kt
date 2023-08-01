package com.leg3nd.application.dto

import com.leg3nd.domain.core.model.Token

data class TokenPairDto(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun fromDomain(token: Token) =
            TokenPairDto(accessToken = token.accessToken, refreshToken = token.refreshToken)
    }
}
