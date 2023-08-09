package com.leg3nd.infrastructure.database.mongo.document

import com.leg3nd.domain.core.model.ServiceEndpoint
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import org.litote.kmongo.toId

@Serializable
data class ServiceEndpointDocument(
    @Contextual
    val _id: Id<ServiceEndpointDocument> = newId(),
    val name: String,
    var basePath: String,
    var publicEndpoints: List<String>,
    var draftEndpoints: List<String>,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
    companion object {
        fun fromDomain(domain: ServiceEndpoint) = ServiceEndpointDocument(
            _id = domain.id?.toId() ?: newId(),
            name = domain.name,
            basePath = domain.basePath,
            publicEndpoints = domain.publicEndpoints,
            draftEndpoints = domain.draftEndpoints,
            createdAt = domain.createdAt.toKotlinLocalDateTime(),
            updatedAt = domain.updatedAt.toKotlinLocalDateTime(),
        )
    }

    fun toDomain() = ServiceEndpoint(
        id = this._id.toString(),
        name = this.name,
        basePath = this.basePath,
        publicEndpoints = this.publicEndpoints,
        draftEndpoints = this.draftEndpoints,
        createdAt = this.createdAt.toJavaLocalDateTime(),
        updatedAt = this.updatedAt.toJavaLocalDateTime(),
    )
}
