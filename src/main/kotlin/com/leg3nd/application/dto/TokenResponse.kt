package com.leg3nd.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
) {
    companion object {
        fun fromTokenPair(tokenPair: TokenPairDto) =
            TokenResponse(accessToken = tokenPair.accessToken)
    }
}
