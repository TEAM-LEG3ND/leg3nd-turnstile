package com.leg3nd.infrastructure.database.mongo.document

import com.leg3nd.domain.core.model.Account
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import org.litote.kmongo.toId
import java.time.LocalDateTime

data class AccountDocument(
    @BsonId
    val id: Id<AccountDocument> = newId(),
    var email: String,
    var nickname: String,
    var fullName: String,
    var oAuthProvider: OAuthProvider,
    var status: Status,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
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
                id = account.id?.toId() ?: newId(),
                email = account.email,
                nickname = account.nickname,
                fullName = account.fullName,
                oAuthProvider = OAuthProvider.fromDomain(account.oAuthProvider),
                status = Status.fromDomain(account.status),
                createdAt = account.createdAt,
                updatedAt = account.updatedAt,
            )
    }

    fun toDomain(): Account =
        Account(
            id = this.id.toString(),
            email = this.email,
            nickname = this.nickname,
            fullName = this.fullName,
            oAuthProvider = this.oAuthProvider.toDomain(),
            status = this.status.toDomain(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
}
