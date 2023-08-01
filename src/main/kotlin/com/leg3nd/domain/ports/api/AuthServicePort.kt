package com.leg3nd.domain.ports.api

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.Token

interface AuthServicePort {
    suspend fun login(oAuthProvider: Account.OAuthProvider, authorizationCode: String): Result<Token>

    suspend fun authenticate(accountId: String, serviceType: Account.Service.ServiceType): Result<String?>

    fun refreshToken(accountId: String): Token
}
