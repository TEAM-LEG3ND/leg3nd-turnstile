package com.leg3nd.domain.ports.database

import com.leg3nd.domain.core.model.ServiceEndpoint
import com.leg3nd.domain.core.model.ServiceType

interface ServiceEndpointDatabasePort {

    suspend fun save(serviceEndpoint: ServiceEndpoint): Result<ServiceEndpoint>

    suspend fun findByServiceType(serviceType: ServiceType): Result<ServiceEndpoint?>
}
