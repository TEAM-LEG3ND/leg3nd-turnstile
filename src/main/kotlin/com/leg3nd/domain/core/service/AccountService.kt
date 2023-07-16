package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.api.AccountServicePort
import com.leg3nd.domain.ports.database.AccountDatabasePort
import org.koin.core.annotation.Single

@Single
class AccountService(
    private val accountDatabasePort: AccountDatabasePort,
) : AccountServicePort {
    override suspend fun create(newAccount: Account): String {
        val createdAccountId = accountDatabasePort.create(newAccount)

        return createdAccountId
    }
}
