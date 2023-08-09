package com.leg3nd.domain.ports.api

import com.leg3nd.domain.core.model.ServiceEndpoint

interface ServiceEndpointServicePort {
    suspend fun upsert(
        name: String,
        basePath: String,
        publicEndpoints: List<String>,
        draftEndpoints: List<String>,
    ): Result<ServiceEndpoint>
}
