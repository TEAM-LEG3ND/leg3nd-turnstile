package com.leg3nd.infrastructure.database.mongo

import com.leg3nd.infrastructure.database.mongo.document.AccountDocument
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection

@Single(createdAtStart = true)
class MongoAccountRepository(
    @Property("mongo.uri") mongoUri: String,
) {
    private val client = KMongo.createClient(mongoUri)
    private val database = client.getDatabase("leg3nd-account")
    private val accountCollection = database.getCollection<AccountDocument>()

    suspend fun create(accountDocument: AccountDocument): String {
        accountCollection.insertOne(accountDocument).awaitFirstOrNull()
            ?: throw Exception("account insert Failed")

        return accountDocument.id.toString()
    }
}
