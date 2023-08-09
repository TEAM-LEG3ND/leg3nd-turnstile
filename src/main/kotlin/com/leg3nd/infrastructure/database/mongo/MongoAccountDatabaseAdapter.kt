package com.leg3nd.infrastructure.database.mongo

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.database.AccountDatabasePort
import com.leg3nd.infrastructure.database.mongo.document.AccountDocument
import org.bson.types.ObjectId
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId
import org.litote.kmongo.setValue

@Single(createdAtStart = true)
class MongoAccountDatabaseAdapter(
    mongoDatabaseConnector: MongoDatabaseConnector,
) : AccountDatabasePort {

    private val accountCollection = mongoDatabaseConnector.database.getCollection<AccountDocument>()

    override suspend fun create(newAccount: Account): String {
        val newAccountDocument = AccountDocument.fromDomain(newAccount)

        accountCollection.insertOne(newAccountDocument)

        return newAccountDocument._id.toString()
    }

    override suspend fun findById(id: String): Result<Account> = runCatching {
        val documentId = id.toObjectId()

        val accountDocument = accountCollection.findOneById(documentId)
            ?: throw NoSuchElementException("Account Document not found with id $documentId")

        accountDocument.toDomain()
    }

    override suspend fun findByEmail(email: String): Result<Account> = runCatching {
        val accountDocument = accountCollection.findOne(AccountDocument::email eq email)
            ?: throw NoSuchElementException("Account Document not found with email $email")

        accountDocument.toDomain()
    }

    override suspend fun updateServicesById(id: String, services: List<Account.Service>): Result<Unit> = runCatching {
        val documentId = id.toObjectId()
        val documentServices = services.map { AccountDocument.Service.fromDomain(it) }

        accountCollection.findOneAndUpdate(
            AccountDocument::_id eq documentId,
            setValue(AccountDocument::services, documentServices),
        )
    }

    private fun String.toObjectId(): Id<AccountDocument> =
        ObjectId(this).toId()
}
