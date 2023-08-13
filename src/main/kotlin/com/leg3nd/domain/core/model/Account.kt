package com.leg3nd.domain.core.model

import java.time.OffsetDateTime

data class Account(
    val id: String? = null,
    var email: String,
    var fullName: String,
    var oAuthProvider: OAuthProvider,
    var status: Status = Status.OK,
    var services: List<Service>,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    data class Service(
        val type: ServiceType,
        var status: Status,
        val createdAt: OffsetDateTime = OffsetDateTime.now(),
        var updatedAt: OffsetDateTime = OffsetDateTime.now(),
    )

    enum class OAuthProvider {
        GOOGLE, GITHUB
    }

    enum class Status {
        DRAFT, OK, SUSPENDED, WITHDRAW
    }
}
