package com.leg3nd.infrastructure.database.mongo

import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

@Single(createdAtStart = true)
class MongoDatabaseConnector(
    @Property("mongo.uri") mongoUri: String,
) {
    private val client = KMongo.createClient(mongoUri).coroutine
    val database = client.getDatabase("leg3nd-account")
}
