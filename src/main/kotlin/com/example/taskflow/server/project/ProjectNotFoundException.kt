package com.example.taskflow.server.project

class ProjectNotFoundException(
    projectId: Long,
) : RuntimeException(
    "Project not found: $projectId"
)