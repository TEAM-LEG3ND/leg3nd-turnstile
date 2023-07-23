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
    var nickname: String,
    var fullName: String,
    var oAuthProvider: OAuthProvider,
    var status: Status,
    var services: List<Service>,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
    @Serializable
    sealed class Service(
        val type: ServiceType,
    ) {
        companion object {
            fun fromDomain(serviceDomain: Account.Service): Service = when (serviceDomain) {
                is Account.Service.Studium -> Studium.fromDomain(serviceDomain)
                is Account.Service.BreadN -> BreadN.fromDomain(serviceDomain)
            }
        }

        fun toDomain(): Account.Service = when (this) {
            is Studium -> this.toStudiumDomain()
            is BreadN -> this.toBreadNDomain()
        }

        enum class ServiceType {
            STUDIUM, BREAD_N
        }

        @Serializable
        data class Studium(
            var status: Status,
            val createdAt: LocalDateTime,
            var updatedAt: LocalDateTime,
        ) : Service(ServiceType.STUDIUM) {
            companion object {
                fun fromDomain(studiumDomain: Account.Service.Studium): Studium =
                    Studium(
                        status = Status.fromDomain(studiumDomain.status),
                        createdAt = studiumDomain.createdAt.toKotlinLocalDateTime(),
                        updatedAt = studiumDomain.updatedAt.toKotlinLocalDateTime(),
                    )
            }

            fun toStudiumDomain(): Account.Service.Studium =
                Account.Service.Studium(
                    status = this.status.toDomain(),
                    createdAt = this.createdAt.toJavaLocalDateTime(),
                    updatedAt = this.updatedAt.toJavaLocalDateTime(),
                )
        }

        @Serializable
        data class BreadN(
            var status: Status,
            val createdAt: LocalDateTime,
            var updatedAt: LocalDateTime,
        ) : Service(ServiceType.BREAD_N) {
            companion object {
                fun fromDomain(breadNDomain: Account.Service.BreadN): BreadN =
                    BreadN(
                        status = Status.fromDomain(breadNDomain.status),
                        createdAt = breadNDomain.createdAt.toKotlinLocalDateTime(),
                        updatedAt = breadNDomain.updatedAt.toKotlinLocalDateTime(),
                    )
            }

            fun toBreadNDomain(): Account.Service.BreadN =
                Account.Service.BreadN(
                    status = this.status.toDomain(),
                    createdAt = this.createdAt.toJavaLocalDateTime(),
                    updatedAt = this.updatedAt.toJavaLocalDateTime(),
                )
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
                nickname = account.nickname,
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
            nickname = this.nickname,
            fullName = this.fullName,
            oAuthProvider = this.oAuthProvider.toDomain(),
            status = this.status.toDomain(),
            services = this.services.map { it.toDomain() },
            createdAt = this.createdAt.toJavaLocalDateTime(),
            updatedAt = this.updatedAt.toJavaLocalDateTime(),
        )
}
