package com.example.taskflow.server.auth

data class RegisterRequest(
    val username: String,
    val password: String,
)