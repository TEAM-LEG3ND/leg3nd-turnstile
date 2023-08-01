package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.OAuthUser
import com.leg3nd.domain.ports.client.OAuthClientPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

@Single
class OAuthService(
    private val oAuthClientPort: OAuthClientPort,
) {
    suspend fun loginWithOAuth(oAuthProvider: Account.OAuthProvider, authorizationCode: String): OAuthUser =
        coroutineScope {
            val oAuthTokenDeferred =
                async(Dispatchers.IO) { oAuthClientPort.getOAuthToken(oAuthProvider, authorizationCode) }
            val oAuthUserDeferred =
                async(Dispatchers.IO) {
                    oAuthClientPort.getOAuthUser(
                        oAuthProvider,
                        oAuthTokenDeferred.await().accessToken,
                    )
                }

            oAuthUserDeferred.await()
        }
}
