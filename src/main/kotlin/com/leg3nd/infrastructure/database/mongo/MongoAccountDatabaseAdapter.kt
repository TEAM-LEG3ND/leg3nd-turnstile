package com.leg3nd.infrastructure.database.mongo

import com.leg3nd.domain.core.model.Account
import com.leg3nd.domain.ports.database.AccountDatabasePort
import com.leg3nd.infrastructure.database.mongo.document.AccountDocument
import org.bson.types.ObjectId
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.setValue
import org.litote.kmongo.toId

@Single(createdAtStart = true)
class MongoAccountDatabaseAdapter(
    @Property("mongo.uri") mongoUri: String,
) : AccountDatabasePort {

    private val client = KMongo.createClient(mongoUri).coroutine
    private val database = client.getDatabase("leg3nd-account")
    private val accountCollection = database.getCollection<AccountDocument>()

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

    override suspend fun updateServicesById(id: String, services: List<Account.Service>): Result<Unit> = runCatching {
        val documentId = id.toId<AccountDocument>()
        val documentServices = services.map { AccountDocument.Service.fromDomain(it) }

        accountCollection.findOneAndUpdate(
            AccountDocument::_id eq documentId,
            setValue(AccountDocument::services, documentServices),
        )
    }

    private fun String.toObjectId(): Id<AccountDocument> =
        ObjectId(this).toId()
}
