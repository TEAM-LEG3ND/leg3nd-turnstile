package com.leg3nd.domain.ports.database

import com.leg3nd.domain.core.model.Account

interface AccountDatabasePort {

    suspend fun create(newAccount: Account): String

    suspend fun findById(id: String): Result<Account>

    suspend fun findByEmail(email: String): Result<Account>

    suspend fun updateServicesById(id: String, services: List<Account.Service>): Result<Unit>
}
