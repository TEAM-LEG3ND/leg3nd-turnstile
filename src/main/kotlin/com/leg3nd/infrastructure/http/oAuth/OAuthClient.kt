package com.leg3nd.infrastructure.http.oAuth

import com.leg3nd.infrastructure.http.oAuth.dto.OAuthTokenResponse
import com.leg3nd.infrastructure.http.oAuth.dto.OAuthUserResponse

interface OAuthClient {
    suspend fun getOAuthToken(code: String): OAuthTokenResponse

    suspend fun getOAuthUser(accessToken: String): OAuthUserResponse
}
