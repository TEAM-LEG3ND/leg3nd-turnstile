package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.ServiceEndpoint
import com.leg3nd.domain.core.model.ServiceType
import com.leg3nd.domain.core.model.Token
import com.leg3nd.domain.ports.service.AccountServicePort
import com.leg3nd.domain.ports.service.AuthServicePort
import com.leg3nd.domain.ports.service.OAuthServicePort
import com.leg3nd.domain.ports.service.ServiceEndpointServicePort
import com.leg3nd.domain.ports.token.TokenManagerPort
import com.leg3nd.domain.ports.token.dto.TokenClaims
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class AuthService(
    private val accountService: AccountServicePort,
    private val tokenManager: TokenManagerPort,
    private val oAuthService: OAuthServicePort,
    private val serviceEndpointServicePort: ServiceEndpointServicePort,
) : AuthServicePort {
    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun login(oAuthProvider: Account.OAuthProvider, authorizationCode: String): Result<Token> =
        runCatching {
            val oAuthUser = oAuthService.loginWithOAuth(oAuthProvider, authorizationCode)

            val accountByEmail = accountService.findAccountByEmail(oAuthUser.email)
            val accountId = if (accountByEmail == null) { // not signed up yet
                val newAccount = Account(
                    email = oAuthUser.email,
                    fullName = oAuthUser.name,
                    oAuthProvider = oAuthProvider,
                    services = emptyList(),
                )
                accountService.create(newAccount)
            } else {
                accountByEmail.id ?: throw Exception("No id found in account with email ${accountByEmail.email}")
            }

            val accessToken = tokenManager.generateAccessToken(accountId)
            val refreshToken = tokenManager.generateRefreshToken(accountId)

            Token(accessToken, refreshToken)
        }

    override suspend fun authenticate(
        accessToken: String?,
        serviceType: ServiceType,
        endpoint: String,
    ): Result<String?> =
        runCatching {
            if (accessToken == null) {
                authenticateIfTokenNotProvided(serviceType, endpoint)
                null
            } else {
                val claims = tokenManager.validateAccessToken(accessToken).getOrElse {
                    log.error("error occurred when validate access token", it)
                    throw it
                }

                authenticateIfTokenProvided(claims, serviceType, endpoint)

                claims.sub
            }
        }

    override fun refreshToken(refreshToken: String): Result<Token> = runCatching {
        val claims = tokenManager.validateRefreshToken(refreshToken).getOrElse {
            log.error("error occurred when validate access token", it)
            throw it
        }

        val accountId = claims.sub

        val generatedAccessToken = tokenManager.generateAccessToken(accountId)
        val generatedRefreshToken = tokenManager.generateRefreshToken(accountId)

        Token(generatedAccessToken, generatedRefreshToken)
    }

    private suspend fun authenticateIfTokenNotProvided(serviceType: ServiceType, endpoint: String) {
        val (serviceEndpoint, routePath) = getServiceEndpointAndRoutePath(serviceType, endpoint)

        if (!serviceEndpoint.publicEndpoints.contains(routePath)) {
            throw Exception("endpoint $endpoint is not public")
        }
    }

    private suspend fun authenticateIfTokenProvided(
        claims: TokenClaims,
        serviceType: ServiceType,
        endpoint: String,
    ) {
        val accountById = accountService.findAccountById(claims.sub)

        val backendService = accountById.services.find { it.type == serviceType }

        if (backendService == null) {
            val (serviceEndpoint, routePath) = getServiceEndpointAndRoutePath(serviceType, endpoint)

            if (!(
                    serviceEndpoint.publicEndpoints.contains(routePath) &&
                        serviceEndpoint.draftEndpoints.contains(routePath)
                    )
            ) {
                throw Exception("endpoint $endpoint is not public")
            }
        } else {
            if (backendService.status != Account.Status.OK) {
                throw Exception("status is not OK for $serviceType")
            }
        }
    }

    private suspend fun getServiceEndpointAndRoutePath(
        serviceType: ServiceType,
        endpoint: String,
    ): Pair<ServiceEndpoint, String> {
        val serviceEndpoint = serviceEndpointServicePort.findByServiceType(serviceType).getOrElse {
            log.error("error occurred findByServiceType", it)
            throw it
        } ?: throw Exception("serviceEndpoint not found")

        val routePath = if (endpoint.startsWith(serviceEndpoint.basePath)) {
            endpoint.removePrefix(serviceEndpoint.basePath)
        } else {
            throw Exception("endpoint $endpoint does not contain base path ${serviceEndpoint.basePath}")
        }
        return Pair(serviceEndpoint, routePath)
    }
}
