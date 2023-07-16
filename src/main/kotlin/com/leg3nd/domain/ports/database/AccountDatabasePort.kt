package com.leg3nd.domain.ports.database

import com.leg3nd.domain.core.model.Account

interface AccountDatabasePort {

    suspend fun create(newAccount: Account): String
}
