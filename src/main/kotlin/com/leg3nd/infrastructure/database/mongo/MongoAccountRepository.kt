package com.leg3nd.infrastructure.database.mongo

import com.leg3nd.infrastructure.database.mongo.document.AccountDocument
import kotlinx.coroutines.reactive.awaitSingle
import org.koin.core.annotation.Single
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection

@Single
class MongoAccountRepository {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("leg3nd-account")
    private val accountCollection = database.getCollection<AccountDocument>()

    suspend fun create(accountDocument: AccountDocument): String {
        val createdAccountDocument = accountCollection.insertOne(accountDocument).awaitSingle()
        return createdAccountDocument.insertedId?.toString() ?: throw Exception("Insert Failed")
    }
}
