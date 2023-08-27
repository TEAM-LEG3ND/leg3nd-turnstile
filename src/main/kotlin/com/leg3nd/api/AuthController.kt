package com.leg3nd.api

import com.leg3nd.api.dto.AuthResponse
import com.leg3nd.api.dto.OAuthLoginRequest
import com.leg3nd.api.dto.TokenPairDto
import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.ServiceType
import com.leg3nd.domain.ports.service.AuthServicePort
import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class AuthController(
    private val authService: AuthServicePort,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun login(oAuthProvider: Account.OAuthProvider, oAuthLoginRequest: OAuthLoginRequest): TokenPairDto {
        val token = authService.login(oAuthProvider, oAuthLoginRequest.code).getOrElse {
            log.error("login failed", it)
            throw BadRequestException("login failed")
        }
        return TokenPairDto.fromDomain(token)
    }

    suspend fun authenticate(accessToken: String?, serviceType: ServiceType, endpoint: String): AuthResponse {
        log.info("accessToken is $accessToken, serviceType is $serviceType, endpoint is $endpoint")
        val authenticatedAccountId = authService.authenticate(accessToken, serviceType, endpoint).getOrElse {
            log.error("authenticate error occurred", it)
            throw BadRequestException("authenticate error occurred", it)
        }
        return AuthResponse(authenticatedAccountId)
    }

    fun refresh(refreshToken: String): TokenPairDto {
        val token = authService.refreshToken(refreshToken).getOrThrow()
        return TokenPairDto.fromDomain(token)
    }
}
