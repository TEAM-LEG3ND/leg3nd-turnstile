package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.Token
import com.leg3nd.domain.ports.api.AuthServicePort
import org.koin.core.annotation.Single

@Single
class AuthService(
    private val accountService: AccountService,
    private val jwtService: JwtService,
    private val oAuthService: OAuthService,
) : AuthServicePort {
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

            val jwtToken = jwtService.generateToken(accountId)
            val refreshToken = jwtService.generateRefreshToken(accountId)

            Token(jwtToken, refreshToken)
        }

    override suspend fun authenticate(accountId: String, serviceType: Account.Service.ServiceType): Result<String?> =
        runCatching {
            val accountById = accountService.findAccountById(accountId)

            val targetService = accountById.services.find { it.type == serviceType }

            if (targetService?.status != Account.Status.OK) {
                // check if service url is public
                val public = true
                if (public) {
                    null
                } else {
                    throw Exception("Authentication Filed")
                }
            } else {
                accountById.id
            }
        }

    override fun refreshToken(accountId: String): Token {
        val jwtToken = jwtService.generateToken(accountId)
        val refreshToken = jwtService.generateRefreshToken(accountId)

        return Token(jwtToken, refreshToken)
    }
}
