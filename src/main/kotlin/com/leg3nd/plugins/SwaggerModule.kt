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
        info {
            title = "Turnstile API Swagger"
            description = "LEG3ND 통합 계정 / 인증 서버 Turnstile Swagger 문서"
        }
    }
}
