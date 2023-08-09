package com.leg3nd.domain.ports.database

import com.leg3nd.domain.core.model.ServiceEndpoint

interface ServiceEndpointDatabasePort {

    suspend fun save(serviceEndpoint: ServiceEndpoint): Result<ServiceEndpoint>

    suspend fun findByName(name: String): Result<ServiceEndpoint?>
}
