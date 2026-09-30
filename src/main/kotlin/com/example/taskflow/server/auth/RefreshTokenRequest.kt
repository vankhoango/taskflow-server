package com.example.taskflow.server.auth

data class RefreshTokenRequest(
    val refreshToken: String,
)