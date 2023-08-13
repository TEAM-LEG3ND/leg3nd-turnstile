package com.leg3nd.api.dto

import com.leg3nd.domain.core.model.Account
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountRequest(
    var email: String,
    var fullName: String,
) {
    fun toDomain(oAuthProviderReq: String): Result<Account> = runCatching {
        val oAuthProvider = Account.OAuthProvider.valueOf(oAuthProviderReq)

        Account(
            email = this.email,
            fullName = this.fullName,
            oAuthProvider = oAuthProvider,
            services = emptyList(),
        )
    }
}
