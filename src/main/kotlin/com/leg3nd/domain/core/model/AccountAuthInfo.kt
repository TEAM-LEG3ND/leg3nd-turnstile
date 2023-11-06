package com.leg3nd.domain.core.model

data class AccountAuthInfo(
    val token: Token,
    val status: Account.Status,
)
