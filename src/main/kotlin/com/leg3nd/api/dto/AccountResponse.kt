package com.leg3nd.api.dto

import com.leg3nd.domain.core.model.Account
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val id: String?,
) {
    companion object {
        fun fromDomain(account: Account) =
            AccountResponse(
                id = account.id,
            )
    }
}
