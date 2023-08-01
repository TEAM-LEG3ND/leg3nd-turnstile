package com.leg3nd.infrastructure.http.oAuth

import com.leg3nd.infrastructure.http.oAuth.dto.OAuthTokenResponse
import com.leg3nd.infrastructure.http.oAuth.dto.OAuthUserResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.util.*
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

@Single
class GoogleOAuthClient(
    @Property("oAuth.google.clientId") private val googleClientId: String,
    @Property("oAuth.google.clientSecret") private val googleClientSecret: String,
    @Property("oAuth.google.redirectUri") private val googleRedirectUri: String,
) : OAuthClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun getOAuthToken(code: String): OAuthTokenResponse {
        val url = "https://accounts.google.com/o/oauth2/token"

        val response: OAuthTokenResponse = client.post(
            url {
                this.host = url
                this.parameters.apply {
                    append("client_id", googleClientId)
                    append("client_secret", googleClientSecret)
                    append("redirect_uri", googleRedirectUri)
                    append("grant_type", "authorization_code")
                    append("code", code)
                }
            },
        ).body()

        return response
    }

    override suspend fun getOAuthUser(accessToken: String): OAuthUserResponse {
        val url = "https://www.googleapis.com/oauth2/v3/userinfo"

        val response: OAuthUserResponse = client.get(
            url,
        ) {
            bearerAuth(url)
        }.body()

        return response
    }
}
