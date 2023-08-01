package com.leg3nd.infrastructure.http.oAuth.dto

import com.leg3nd.domain.ports.client.dto.OAuthTokenPortResponse
import kotlinx.serialization.Serializable

@Serializable
data class OAuthTokenResponse(
    val accessToken: String,
) {
    fun toPortResponse() = OAuthTokenPortResponse(accessToken)
}
