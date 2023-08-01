package com.leg3nd.infrastructure.http.oAuth

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.OAuthUser
import com.leg3nd.domain.ports.client.OAuthClientPort
import com.leg3nd.domain.ports.client.dto.OAuthTokenPortResponse
import org.koin.core.annotation.Single

@Single
class OAuthClientAdapter(
    private val googleOAuthClient: GoogleOAuthClient,
) : OAuthClientPort {
    override suspend fun getOAuthToken(oAuthProvider: Account.OAuthProvider, code: String): OAuthTokenPortResponse =
        when (oAuthProvider) {
            Account.OAuthProvider.GOOGLE -> googleOAuthClient.getOAuthToken(code).toPortResponse()
            else -> throw Exception("Not supported oAuthProvider: $oAuthProvider")
        }

    override suspend fun getOAuthUser(
        oAuthProvider: Account.OAuthProvider,
        oAuthAccessToken: String,
    ): OAuthUser = when (oAuthProvider) {
        Account.OAuthProvider.GOOGLE -> googleOAuthClient.getOAuthUser(oAuthAccessToken).toDomain()
        else -> throw Exception("Not supported oAuthProvider: $oAuthProvider")
    }
}
