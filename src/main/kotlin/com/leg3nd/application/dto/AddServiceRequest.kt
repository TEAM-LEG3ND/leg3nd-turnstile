package com.leg3nd.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddServiceRequest(
    val accountId: String,
    val serviceType: String,
)
