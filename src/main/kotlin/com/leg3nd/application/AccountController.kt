package com.leg3nd.application

import com.leg3nd.application.dto.CreateAccountRequest
import com.leg3nd.domain.ports.api.AccountServicePort
import org.koin.core.annotation.Single

@Single
class AccountController(
    private val accountServicePort: AccountServicePort,
) {
    suspend fun create(createAccountRequest: CreateAccountRequest): String {
        val newAccount = createAccountRequest.toDomain("GOOGLE").getOrThrow()

        return accountServicePort.create(newAccount)
    }
}
