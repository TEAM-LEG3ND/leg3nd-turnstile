package com.leg3nd.infrastructure.http.oAuth.dto

import com.leg3nd.domain.core.model.OAuthUser
import kotlinx.serialization.Serializable

@Serializable
data class OAuthUserResponse(val email: String, val name: String) {
    fun toDomain() = OAuthUser(email = email, name = name)
}
