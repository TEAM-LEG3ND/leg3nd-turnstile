package com.leg3nd.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "Token Response Dto")
data class TokenResponse(
    @field:Schema(name = "access_token")
    val accessToken: String,
) {
    companion object {
        fun fromTokenPair(tokenPair: TokenPairDto) =
            TokenResponse(accessToken = tokenPair.accessToken)
    }
}
