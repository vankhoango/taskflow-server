package com.example.taskflow.server.project

fun Project.toResponse(): ProjectResponse {
    return ProjectResponse(
        id = id,
        name = name,
    )
}