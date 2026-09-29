package com.example.taskflow.server.auth

data class LoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
)