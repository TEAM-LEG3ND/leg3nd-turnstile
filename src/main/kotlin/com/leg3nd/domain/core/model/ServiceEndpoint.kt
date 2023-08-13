package com.leg3nd.domain.core.model

import java.time.OffsetDateTime

data class ServiceEndpoint(
    val id: String? = null,
    val serviceType: ServiceType,
    var basePath: String,
    var publicEndpoints: List<String>,
    var draftEndpoints: List<String>,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
