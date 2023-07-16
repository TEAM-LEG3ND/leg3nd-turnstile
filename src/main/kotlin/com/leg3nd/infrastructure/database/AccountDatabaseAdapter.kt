package com.leg3nd.infrastructure.database

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.database.AccountDatabasePort
import com.leg3nd.infrastructure.database.mongo.MongoAccountRepository
import com.leg3nd.infrastructure.database.mongo.document.AccountDocument

class AccountDatabaseAdapter(
    private val mongoAccountRepository: MongoAccountRepository,
) : AccountDatabasePort {
    override suspend fun create(newAccount: Account): String {
        val newAccountDocument = AccountDocument.fromDomain(newAccount)
        return mongoAccountRepository.create(newAccountDocument)
    }
}
