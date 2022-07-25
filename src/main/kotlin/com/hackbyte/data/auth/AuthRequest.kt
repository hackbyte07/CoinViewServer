package com.hackbyte.data.auth

@kotlinx.serialization.Serializable
data class AuthRequest(
    val username: String,
    val email: String = "",
    val password: String
)
