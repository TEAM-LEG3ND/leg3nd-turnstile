package com.leg3nd.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginRequest(
    val code: String,
)
