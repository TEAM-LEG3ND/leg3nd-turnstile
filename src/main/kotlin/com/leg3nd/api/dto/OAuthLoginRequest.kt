package com.leg3nd.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginRequest(
    val code: String,
)
