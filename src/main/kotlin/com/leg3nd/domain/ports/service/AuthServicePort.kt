package com.leg3nd.domain.ports.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.AccountAuthInfo
import com.leg3nd.domain.core.model.ServiceType

interface AuthServicePort {
    suspend fun login(oAuthProvider: Account.OAuthProvider, authorizationCode: String): Result<AccountAuthInfo>
    suspend fun authenticate(accessToken: String?, serviceType: ServiceType, endpoint: String): Result<String?>

    fun refreshToken(refreshToken: String): Result<AccountAuthInfo>
}
