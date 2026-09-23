package com.example.taskflow.server.task

data class UpdateTaskRequest(
    val title: String? = null,
    val completed: Boolean? = null,
)