package com.leg3nd.domain.ports.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.ServiceType
import com.leg3nd.domain.core.model.Token

interface AuthServicePort {
    suspend fun login(oAuthProvider: Account.OAuthProvider, authorizationCode: String): Result<Token>
    suspend fun authenticate(accessToken: String?, serviceType: ServiceType, endpoint: String): Result<String?>

    fun refreshToken(refreshToken: String): Result<Token>
}
