package com.leg3nd.application

import com.leg3nd.application.dto.ServiceEndpointResponse
import com.leg3nd.application.dto.UpsertServiceEndpointRequest
import com.leg3nd.domain.ports.api.ServiceEndpointServicePort
import org.koin.core.annotation.Single

@Single
class ServiceEndpointController(
    private val serviceEndpointServicePort: ServiceEndpointServicePort,
) {

    suspend fun upsert(upsertServiceEndpointRequest: UpsertServiceEndpointRequest): ServiceEndpointResponse {
        val upsertServiceEndpoint = serviceEndpointServicePort.upsert(
            upsertServiceEndpointRequest.name,
            upsertServiceEndpointRequest.basePath,
            upsertServiceEndpointRequest.publicEndpoints,
            upsertServiceEndpointRequest.draftEndpoints,
        ).getOrThrow()

        return ServiceEndpointResponse.fromDomain(upsertServiceEndpoint)
    }
}
