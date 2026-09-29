package com.example.taskflow.server.common.api

data class ApiErrorResponse(
    val code: String,
    val message: String,
)