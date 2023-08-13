package com.leg3nd.common.util

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

object DateTimeUtil {
    fun OffsetDateTime.toEpochMilli() =
        this.toInstant().toEpochMilli()

    fun Long.toOffsetDateTime(zone: ZoneId = ZoneId.systemDefault()): OffsetDateTime =
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), zone)

    fun Date.toOffsetDateTime(zoneOffset: ZoneOffset = ZoneOffset.UTC): OffsetDateTime =
        this.toInstant().atOffset(zoneOffset)
}
