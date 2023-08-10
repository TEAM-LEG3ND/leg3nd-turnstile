package com.leg3nd.domain.core.service

import com.leg3nd.domain.core.model.ServiceEndpoint
import com.leg3nd.domain.ports.api.ServiceEndpointServicePort
import com.leg3nd.domain.ports.database.ServiceEndpointDatabasePort
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

@Single
class ServiceEndpointService(
    private val serviceEndpointDatabasePort: ServiceEndpointDatabasePort,
) : ServiceEndpointServicePort {
    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun upsert(
        name: String,
        basePath: String,
        publicEndpoints: List<String>,
        draftEndpoints: List<String>,
    ): Result<ServiceEndpoint> = runCatching {
        val foundServiceEndpoint = serviceEndpointDatabasePort.findByName(name).getOrElse {
            log.error("findByName error occurred", it)
            throw it
        }

        val serviceEndpointToSave = foundServiceEndpoint?.let {
            ServiceEndpoint(
                id = it.id,
                name = it.name,
                basePath = basePath,
                publicEndpoints = publicEndpoints,
                draftEndpoints = draftEndpoints,
                createdAt = it.createdAt,
                updatedAt = OffsetDateTime.now(),
            )
        } ?: ServiceEndpoint(
            name = name,
            basePath = basePath,
            publicEndpoints = publicEndpoints,
            draftEndpoints = draftEndpoints,
        )

        serviceEndpointDatabasePort.save(serviceEndpointToSave).getOrElse {
            log.error("save serviceEndpoint error occurred", it)
            throw it
        }
    }
}
