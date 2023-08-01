package com.leg3nd.infrastructure.http.oAuth

import com.leg3nd.infrastructure.http.oAuth.dto.OAuthTokenResponse
import com.leg3nd.infrastructure.http.oAuth.dto.OAuthUserResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

@Single
class GoogleOAuthClient(
    @Property("oAuth.google.clientId") private val googleClientId: String,
    @Property("oAuth.google.clientSecret") private val googleClientSecret: String,
    @Property("oAuth.google.redirectUri") private val googleRedirectUri: String,
) : OAuthClient {

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    namingStrategy = JsonNamingStrategy.SnakeCase
                    ignoreUnknownKeys = true
                },
            )
        }
    }

    override suspend fun getOAuthToken(code: String): OAuthTokenResponse {
        val url = "https://accounts.google.com/o/oauth2/token?" +
            "client_id=$googleClientId" +
            "&client_secret=$googleClientSecret" +
            "&redirect_uri=$googleRedirectUri" +
            "&grant_type=authorization_code" +
            "&code=$code"

        return client.post(url).body<OAuthTokenResponse>()
    }

    override suspend fun getOAuthUser(accessToken: String): OAuthUserResponse {
        val url = "https://www.googleapis.com/oauth2/v3/userinfo"

        val response: OAuthUserResponse = client.get(
            url,
        ) {
            bearerAuth(accessToken)
        }.body()

        return response
    }
}
