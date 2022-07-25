package com.hackbyte.security.token

import kotlin.time.Duration.Companion.days


data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long = 365.days.inWholeMilliseconds,
    val secret: String // server only
)
