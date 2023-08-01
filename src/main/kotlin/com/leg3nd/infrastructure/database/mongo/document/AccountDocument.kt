package com.leg3nd.infrastructure.database.mongo.document

import com.leg3nd.domain.core.model.Account
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import org.litote.kmongo.toId

@Serializable
data class AccountDocument(
    @Contextual
    val _id: Id<AccountDocument> = newId(),
    var email: String,
    var fullName: String,
    var oAuthProvider: OAuthProvider,
    var status: Status,
    var services: List<Service>,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
    @Serializable
    data class Service(
        val type: ServiceType,
        var status: Status,
        val createdAt: LocalDateTime,
        var updatedAt: LocalDateTime,
    ) {
        companion object {
            fun fromDomain(serviceDomain: Account.Service): Service =
                Service(
                    type = ServiceType.fromDomain(serviceDomain.type),
                    status = Status.fromDomain(serviceDomain.status),
                    createdAt = serviceDomain.createdAt.toKotlinLocalDateTime(),
                    updatedAt = serviceDomain.updatedAt.toKotlinLocalDateTime(),
                )
        }

        fun toDomain(): Account.Service =
            Account.Service(
                type = this.type.toDomain(),
                status = this.status.toDomain(),
                createdAt = this.createdAt.toJavaLocalDateTime(),
                updatedAt = this.updatedAt.toJavaLocalDateTime(),
            )

        enum class ServiceType {
            STUDIUM, BREAD_N
            ;

            companion object {
                fun fromDomain(serviceTypeDomain: Account.Service.ServiceType): ServiceType = when (serviceTypeDomain) {
                    Account.Service.ServiceType.STUDIUM -> STUDIUM
                    Account.Service.ServiceType.BREAD_N -> BREAD_N
                }
            }

            fun toDomain(): Account.Service.ServiceType = when (this) {
                STUDIUM -> Account.Service.ServiceType.STUDIUM
                BREAD_N -> Account.Service.ServiceType.BREAD_N
            }
        }
    }

    enum class OAuthProvider {
        GOOGLE, GITHUB
        ;

        companion object {
            fun fromDomain(oAuthProvider: Account.OAuthProvider): OAuthProvider = when (oAuthProvider) {
                Account.OAuthProvider.GOOGLE -> GOOGLE
                Account.OAuthProvider.GITHUB -> GITHUB
            }
        }

        fun toDomain(): Account.OAuthProvider = when (this) {
            GOOGLE -> Account.OAuthProvider.GOOGLE
            GITHUB -> Account.OAuthProvider.GITHUB
        }
    }

    enum class Status {
        DRAFT, OK, SUSPENDED, WITHDRAW
        ;

        companion object {
            fun fromDomain(status: Account.Status): Status = when (status) {
                Account.Status.DRAFT -> DRAFT
                Account.Status.OK -> OK
                Account.Status.SUSPENDED -> SUSPENDED
                Account.Status.WITHDRAW -> WITHDRAW
            }
        }

        fun toDomain(): Account.Status = when (this) {
            DRAFT -> Account.Status.DRAFT
            OK -> Account.Status.OK
            SUSPENDED -> Account.Status.SUSPENDED
            WITHDRAW -> Account.Status.WITHDRAW
        }
    }

    companion object {
        fun fromDomain(account: Account): AccountDocument =
            AccountDocument(
                _id = account.id?.toId() ?: newId(),
                email = account.email,
                fullName = account.fullName,
                oAuthProvider = OAuthProvider.fromDomain(account.oAuthProvider),
                status = Status.fromDomain(account.status),
                services = account.services.map { Service.fromDomain(it) },
                createdAt = account.createdAt.toKotlinLocalDateTime(),
                updatedAt = account.updatedAt.toKotlinLocalDateTime(),
            )
    }

    fun toDomain(): Account =
        Account(
            id = this._id.toString(),
            email = this.email,
            fullName = this.fullName,
            oAuthProvider = this.oAuthProvider.toDomain(),
            status = this.status.toDomain(),
            services = this.services.map { it.toDomain() },
            createdAt = this.createdAt.toJavaLocalDateTime(),
            updatedAt = this.updatedAt.toJavaLocalDateTime(),
        )
}
