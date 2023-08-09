package com.leg3nd.domain.core.model

import java.time.LocalDateTime

data class ServiceEndpoint(
    val id: String? = null,
    val name: String,
    var basePath: String,
    var publicEndpoints: List<String>,
    var draftEndpoints: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
