package com.leg3nd.infrastructure.token.config

import com.leg3nd.infrastructure.token.JwtTokenManagerConfig
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
) : JwtTokenManagerConfig {
    override val duration: Duration = Duration.parse(durationRaw)
    override val refreshDuration: Duration = Duration.parse(refreshDurationRaw)
}
