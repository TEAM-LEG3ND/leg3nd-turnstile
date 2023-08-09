package com.leg3nd.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema
data class UpsertServiceEndpointRequest(
    @field:Schema(name = "name")
    val name: String,
    @field:Schema(name = "base_path")
    val basePath: String,
    @field:Schema(name = "public_endpoints")
    val publicEndpoints: List<String>,
    @field:Schema(name = "draft_endpoints")
    val draftEndpoints: List<String>,
)
