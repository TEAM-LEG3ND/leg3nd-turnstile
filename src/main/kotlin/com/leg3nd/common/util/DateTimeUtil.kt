package com.leg3nd.common.util

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

object DateTimeUtil {
    fun OffsetDateTime.toEpochMilli() =
        this.toInstant().toEpochMilli()

    fun Long.toOffsetDateTime(zone: ZoneId = ZoneId.systemDefault()): OffsetDateTime =
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), zone)
