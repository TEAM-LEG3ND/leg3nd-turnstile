package com.leg3nd.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "Check Token Response Dto")
data class AuthResponse(

    @field:Schema(name = "account_id")
    val accountId: String?,
)
