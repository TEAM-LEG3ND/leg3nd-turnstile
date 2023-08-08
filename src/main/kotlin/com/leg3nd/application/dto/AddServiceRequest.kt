package com.leg3nd.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(title = "Add Service Request Dto")
data class AddServiceRequest(

    @field:Schema(name = "account_id", description = "LEG3ND Universal Account Id", required = true)
    val accountId: String,

    @field:Schema(name = "service_type", description = "LEG3ND Service Type", required = true)
    val serviceType: String,
)
