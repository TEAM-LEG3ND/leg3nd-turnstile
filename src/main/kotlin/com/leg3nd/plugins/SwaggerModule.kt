package com.leg3nd.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.AuthScheme
import io.github.smiley4.ktorswaggerui.dsl.AuthType
import io.github.smiley4.ktorswaggerui.dsl.SwaggerUiSort
import io.github.smiley4.ktorswaggerui.dsl.SwaggerUiSyntaxHighlight
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSwagger() {
    val jwtAuthSchemeName = "auth-jwt"

    install(SwaggerUI) {
        swagger {
            rootHostPath = "/turnstile"
            forwardRoot = false
            swaggerUrl = "swagger-ui"
            onlineSpecValidator()
            displayOperationId = false
            showTagFilterInput = true
            sort = SwaggerUiSort.HTTP_METHOD
            syntaxHighlight = SwaggerUiSyntaxHighlight.MONOKAI
        }
        securityScheme(jwtAuthSchemeName) {
            type = AuthType.HTTP
            scheme = AuthScheme.BEARER
            bearerFormat = "jwt"
        }
        server {
            url = "https://api.server.d0lim.com/turnstile/"
            description = "Dev(mac mini)"
        }
        server {
            url = "http://localhost:8080"
            description = "Local"
        }
    }
}
