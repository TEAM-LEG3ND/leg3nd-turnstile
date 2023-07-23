package com.leg3nd.application

import com.leg3nd.application.dto.AddServiceRequest
import com.leg3nd.application.dto.CreateAccountRequest
import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.api.AccountServicePort
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

    private fun String.toServiceType(): Account.Service.ServiceType =
        Account.Service.ServiceType.valueOf(this)
}
