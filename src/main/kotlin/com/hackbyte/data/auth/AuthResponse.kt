package com.hackbyte.data.auth

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String
)
