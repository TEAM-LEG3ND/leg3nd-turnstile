package com.leg3nd.domain.ports.service

import com.leg3nd.domain.core.model.Account

interface AccountServicePort {
    suspend fun create(newAccount: Account): String

    suspend fun addService(accountId: String, serviceType: Account.Service.ServiceType)

    suspend fun findAccountById(accountId: String): Account

    suspend fun findAccountByEmail(email: String): Account?
}