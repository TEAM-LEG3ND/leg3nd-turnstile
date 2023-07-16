package com.leg3nd.infrastructure.database.mongo.document

import com.leg3nd.domain.core.model.Account
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.time.LocalDateTime

data class AccountDocument(
    @BsonId
    val id: Id<AccountDocument>?,
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

        fun toDomain(): Account.OAuthProvider = when (this) {
            GOOGLE -> Account.OAuthProvider.GOOGLE
            GITHUB -> Account.OAuthProvider.GITHUB
        }
    }

    enum class Status {
        DRAFT, OK, SUSPENDED, WITHDRAW
        ;

        fun toDomain(): Account.Status = when (this) {
            DRAFT -> Account.Status.DRAFT
            OK -> Account.Status.OK
            SUSPENDED -> Account.Status.SUSPENDED
            WITHDRAW -> Account.Status.WITHDRAW
        }
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
