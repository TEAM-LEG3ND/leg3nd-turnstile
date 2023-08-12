package com.leg3nd.domain.ports.api

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.OAuthUser

interface OAuthServicePort {
    suspend fun loginWithOAuth(oAuthProvider: Account.OAuthProvider, authorizationCode: String): OAuthUser
}
