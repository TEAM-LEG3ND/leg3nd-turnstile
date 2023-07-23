package com.leg3nd.domain.ports.api

import com.leg3nd.domain.core.model.Account

interface AccountServicePort {
    suspend fun create(newAccount: Account): String

    suspend fun addService(accountId: String, serviceType: Account.Service.ServiceType)
}
