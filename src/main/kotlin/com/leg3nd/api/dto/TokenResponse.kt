package com.leg3nd.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "Token Response Dto")
data class TokenResponse(
    @field:Schema(name = "access_token")
    val accessToken: String,
    @field:Schema(name = "status")
    val status: String,
) {
    companion object {
        fun fromTokenPair(tokenPair: TokenPairDto) =
            TokenResponse(accessToken = tokenPair.accessToken, status = tokenPair.status)
    }
}
