package com.leg3nd.api.dto

import com.leg3nd.domain.core.model.AccountAuthInfo

data class TokenPairDto(
    val accessToken: String,
    val refreshToken: String,
    val status: String,
) {
    companion object {
        fun fromAccountAuthInfo(accountAuthInfo: AccountAuthInfo) =
            TokenPairDto(
                accessToken = accountAuthInfo.token.accessToken,
                refreshToken = accountAuthInfo.token.refreshToken,
                status = accountAuthInfo.status.name,
            )
    }
}
