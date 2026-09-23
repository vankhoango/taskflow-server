package com.example.taskflow.server.task

fun Task.toResponse(): TaskResponse {
    return TaskResponse(
        id = id,
        projectId = project.id,
        title = title,
        completed = completed,
    )
}