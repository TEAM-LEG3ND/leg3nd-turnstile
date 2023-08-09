package com.leg3nd.infrastructure.database.mongo

import com.leg3nd.domain.core.model.ServiceEndpoint
import com.leg3nd.domain.ports.database.ServiceEndpointDatabasePort
import com.leg3nd.infrastructure.database.mongo.document.ServiceEndpointDocument
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

@Single(createdAtStart = true)
class MongoServiceEndpointDatabaseAdapter(
    mongoDatabaseConnector: MongoDatabaseConnector,
) : ServiceEndpointDatabasePort {
    private val serviceEndpointCollection = mongoDatabaseConnector.database.getCollection<ServiceEndpointDocument>()

    override suspend fun save(serviceEndpoint: ServiceEndpoint): Result<ServiceEndpoint> = runCatching {
        val serviceEndpointDocumentToSave = ServiceEndpointDocument.fromDomain(serviceEndpoint)

        serviceEndpointCollection.save(serviceEndpointDocumentToSave)

        serviceEndpoint
    }

    override suspend fun findByName(name: String): Result<ServiceEndpoint?> = runCatching {
        serviceEndpointCollection.findOne(ServiceEndpointDocument::name eq name)?.toDomain()
    }
}
