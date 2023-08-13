package com.leg3nd.domain.ports.service

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.core.model.ServiceType

interface AccountServicePort {
    suspend fun create(newAccount: Account): String

    suspend fun addService(accountId: String, serviceType: ServiceType)

    suspend fun findAccountById(accountId: String): Account

    suspend fun findAccountByEmail(email: String): Account?
}
