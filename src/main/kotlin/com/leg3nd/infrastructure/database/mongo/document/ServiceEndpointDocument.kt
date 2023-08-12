package com.leg3nd.infrastructure.database.mongo.document

import com.leg3nd.common.util.DateTimeUtil.toEpochMilli
import com.leg3nd.common.util.DateTimeUtil.toOffsetDateTime
import com.leg3nd.domain.core.model.ServiceEndpoint
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId

@Serializable
data class ServiceEndpointDocument(
    @Contextual
    val _id: Id<ServiceEndpointDocument> = newId(),
    val name: String,
    var basePath: String,
    var publicEndpoints: List<String>,
    var draftEndpoints: List<String>,
    val createdAt: Long,
    var updatedAt: Long,
) {
    companion object {
        fun fromDomain(domain: ServiceEndpoint) = ServiceEndpointDocument(
            _id = domain.id?.toObjectId() ?: newId(),
            name = domain.name,
            basePath = domain.basePath,
            publicEndpoints = domain.publicEndpoints,
            draftEndpoints = domain.draftEndpoints,
            createdAt = domain.createdAt.toEpochMilli(),
            updatedAt = domain.updatedAt.toEpochMilli(),
        )
    }

    fun toDomain() = ServiceEndpoint(
        id = this._id.toString(),
        name = this.name,
        basePath = this.basePath,
        publicEndpoints = this.publicEndpoints,
        draftEndpoints = this.draftEndpoints,
        createdAt = this.createdAt.toOffsetDateTime(),
        updatedAt = this.updatedAt.toOffsetDateTime(),
    )
}

private fun String.toObjectId(): Id<ServiceEndpointDocument> =
    ObjectId(this).toId()
