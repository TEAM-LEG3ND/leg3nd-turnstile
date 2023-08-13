package com.leg3nd.api.dto

import com.leg3nd.domain.core.model.ServiceEndpoint
import com.leg3nd.domain.core.model.ServiceType
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

@Serializable
@Schema
data class ServiceEndpointResponse(
    @field:Schema(name = "id")
    val id: String?,
    @field:Schema(name = "type")
    val type: ServiceType,
    @field:Schema(name = "base_path")
    val basePath: String,
    @field:Schema(name = "public_endpoints")
    val publicEndpoints: List<String>,
    @field:Schema(name = "draft_endpoints")
    val draftEndpoints: List<String>,
    @field:Schema(name = "created_at")
    val createdAt: String,
    @field:Schema(name = "updated_at")
    val updatedAt: String,
) {
    companion object {
        fun fromDomain(domain: ServiceEndpoint) = ServiceEndpointResponse(
            id = domain.id,
            type = domain.serviceType,
            basePath = domain.basePath,
            publicEndpoints = domain.publicEndpoints,
            draftEndpoints = domain.draftEndpoints,
            createdAt = domain.createdAt.format(DateTimeFormatter.ISO_DATE_TIME),
            updatedAt = domain.updatedAt.format(DateTimeFormatter.ISO_DATE_TIME),
        )
    }
}
