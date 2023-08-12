package com.leg3nd.api.config

import com.leg3nd.domain.core.service.JwtServiceConfig
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import kotlin.time.Duration

@Single
class JwtConfig(
    @Property("jwt.secret") override val secret: String,
    @Property("jwt.refreshSecret") override val refreshSecret: String,
    @Property("jwt.audience") override val audience: String,
    @Property("jwt.duration") durationRaw: String,
    @Property("jwt.refreshDuration") refreshDurationRaw: String,
) : JwtServiceConfig {
    override val duration: Duration = Duration.parse(durationRaw)
    override val refreshDuration: Duration = Duration.parse(refreshDurationRaw)
}
