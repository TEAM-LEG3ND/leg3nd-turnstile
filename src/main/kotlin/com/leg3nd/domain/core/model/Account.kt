package com.leg3nd.domain.core.model

import java.time.LocalDateTime

data class Account(
    val id: String? = null,
    var email: String,
    var nickname: String,
    var fullName: String,
    var oAuthProvider: OAuthProvider,
    var status: Status = Status.OK,
    var services: List<Service>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    data class Service(
        val type: ServiceType,
        var status: Status,
        val createdAt: LocalDateTime = LocalDateTime.now(),
        var updatedAt: LocalDateTime = LocalDateTime.now(),
    ) {

        enum class ServiceType {
            STUDIUM, BREAD_N
        }
    }

    enum class OAuthProvider {
        GOOGLE, GITHUB
    }

    enum class Status {
        DRAFT, OK, SUSPENDED, WITHDRAW
    }
}
