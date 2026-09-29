package com.example.taskflow.server.auth

data class LoginRequest(
    val username: String,
    val password: String,
)