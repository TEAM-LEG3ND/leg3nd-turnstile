package com.leg3nd.application

import com.leg3nd.application.dto.AuthResponse
import com.leg3nd.application.dto.OAuthLoginRequest
import com.leg3nd.application.dto.TokenPairDto
import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.service.AuthService
import org.koin.core.annotation.Single

@Single
class AuthController(
    private val authService: AuthService,
) {
    suspend fun login(oAuthProvider: Account.OAuthProvider, oAuthLoginRequest: OAuthLoginRequest): TokenPairDto {
        val token = authService.login(oAuthProvider, oAuthLoginRequest.code).getOrElse {
            throw Exception("login failed")
        }
        return TokenPairDto.fromDomain(token)
    }

    suspend fun authenticate(accountId: String, serviceType: Account.Service.ServiceType): AuthResponse {
        val authenticatedAccountId = authService.authenticate(accountId, serviceType).getOrThrow()
        return AuthResponse(authenticatedAccountId)
    }

    fun refresh(accountId: String): TokenPairDto {
        val token = authService.refreshToken(accountId)
        return TokenPairDto.fromDomain(token)
    }
}
