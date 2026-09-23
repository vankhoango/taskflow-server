package com.example.taskflow.server.task

data class TaskResponse(
    val id: Long,
    val projectId: Long,
    val title: String,
    val completed: Boolean,
)