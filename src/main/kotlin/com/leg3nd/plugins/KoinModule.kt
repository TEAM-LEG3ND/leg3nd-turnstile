package com.leg3nd.plugins

import io.ktor.server.application.*
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.fileProperties
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

@Module
@ComponentScan("com.leg3nd")
class KoinModule

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(KoinModule().module)
        fileProperties()
    }
}
