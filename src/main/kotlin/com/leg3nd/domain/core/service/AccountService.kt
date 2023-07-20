package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.api.AccountServicePort
import com.leg3nd.domain.ports.database.AccountDatabasePort
import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class AccountService(
    private val accountDatabasePort: AccountDatabasePort,
) : AccountServicePort {
    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun create(newAccount: Account): String {
        val createdAccountId = accountDatabasePort.create(newAccount)

        return createdAccountId
    }

    override suspend fun addService(accountId: String, service: Account.Service) {
        val account = accountDatabasePort.findById(accountId).getOrElse {
            log.error("error occurred when findById", it)
            throw BadRequestException("No such user with account id")
        }

        val newServices = account.services + listOf(service)
        accountDatabasePort.updateServicesById(accountId, newServices)
    }
}
