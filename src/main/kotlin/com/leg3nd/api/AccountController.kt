package com.leg3nd.api

import com.leg3nd.api.dto.AccountResponse
import com.leg3nd.api.dto.AddServiceRequest
import com.leg3nd.api.dto.CreateAccountRequest
import com.leg3nd.domain.core.model.ServiceType
import com.leg3nd.domain.ports.service.AccountServicePort
import io.ktor.server.plugins.*
import org.koin.core.annotation.Single

@Single
class AccountController(
    private val accountServicePort: AccountServicePort,
) {
    suspend fun create(createAccountRequest: CreateAccountRequest): String {
        val newAccount = createAccountRequest.toDomain("GOOGLE").getOrThrow()

        return accountServicePort.create(newAccount)
    }

    suspend fun addService(addServiceRequest: AddServiceRequest) {
        val serviceType = runCatching { addServiceRequest.serviceType.toServiceType() }
            .getOrElse {
                throw BadRequestException("Please check ServiceType", it)
            }
        accountServicePort.addService(addServiceRequest.accountId, serviceType)
    }

    suspend fun getAccountById(accountId: String): AccountResponse {
        return AccountResponse.fromDomain(accountServicePort.findAccountById(accountId))
    }

    private fun String.toServiceType(): ServiceType =
        ServiceType.valueOf(this)
}
