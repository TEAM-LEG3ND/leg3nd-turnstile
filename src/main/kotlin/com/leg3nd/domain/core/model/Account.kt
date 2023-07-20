package com.leg3nd.domain.core.model

import java.time.LocalDateTime

data class Account(
    val id: String? = null,
    var email: String,
    var nickname: String,
    var fullName: String,
    var oAuthProvider: OAuthProvider,
    var status: Status = Status.DRAFT,
    var services: List<Service>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    sealed class Service(
        val type: ServiceType,
    ) {
        data class Studium(
            val nickname: String,
            val profileImageUrl: String,
            var status: Status,
            val intro: String,
            val createdAt: LocalDateTime = LocalDateTime.now(),
            var updatedAt: LocalDateTime = LocalDateTime.now(),
        ) : Service(ServiceType.STUDIUM)

        data class BreadN(
            val nickname: String,
            val profileImageUrl: String,
            var status: Status,
            val createdAt: LocalDateTime = LocalDateTime.now(),
            var updatedAt: LocalDateTime = LocalDateTime.now(),
        ) : Service(ServiceType.BREAD_N)

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
