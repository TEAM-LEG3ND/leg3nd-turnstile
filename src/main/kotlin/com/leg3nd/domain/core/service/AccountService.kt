package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.api.AccountServicePort
import com.leg3nd.domain.ports.database.AccountDatabasePort
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

@Single
class AccountService(
    private val accountDatabasePort: AccountDatabasePort,
) : AccountServicePort {
    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun create(newAccount: Account): String {
        val createdAccountId = accountDatabasePort.create(newAccount)

        return createdAccountId
    }

    override suspend fun addService(accountId: String, serviceType: Account.Service.ServiceType) {
        val account = accountDatabasePort.findById(accountId).getOrElse {
            log.error("error occurred when findById", it)
            throw Exception("No such user with account id")
        }

        if (account.services.map { it.type }.contains(serviceType)) {
            val msg = "Service already exist"
            log.error(msg)
            throw Exception(msg)
        }

        val service = Account.Service(
            type = serviceType,
            status = Account.Status.DRAFT,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )

        val newServices = account.services + listOf(service)
        accountDatabasePort.updateServicesById(accountId, newServices)
    }

    override suspend fun findAccountById(accountId: String): Account {
        val account = accountDatabasePort.findById(accountId).getOrElse {
            log.error("error occurred when findById", it)
            throw Exception("No such user with account id")
        }

        return account
    }
}
