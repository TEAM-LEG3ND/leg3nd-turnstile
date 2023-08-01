package com.leg3nd.domain.ports.client

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.OAuthUser
import com.leg3nd.domain.ports.client.dto.OAuthTokenPortResponse

interface OAuthClientPort {
    suspend fun getOAuthToken(oAuthProvider: Account.OAuthProvider, code: String): OAuthTokenPortResponse

    suspend fun getOAuthUser(oAuthProvider: Account.OAuthProvider, oAuthAccessToken: String): OAuthUser
}
