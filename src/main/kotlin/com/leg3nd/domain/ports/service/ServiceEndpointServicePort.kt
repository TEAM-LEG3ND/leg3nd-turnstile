package com.leg3nd.domain.ports.service

import com.leg3nd.domain.core.model.ServiceEndpoint
import com.leg3nd.domain.core.model.ServiceType

interface ServiceEndpointServicePort {
    suspend fun upsert(
        serviceType: ServiceType,
        basePath: String,
        publicEndpoints: List<String>,
        draftEndpoints: List<String>,
    ): Result<ServiceEndpoint>

    suspend fun findByServiceType(serviceType: ServiceType): Result<ServiceEndpoint?>
}
