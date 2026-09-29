package com.example.taskflow.server.task

class TaskNotFoundException(
    taskId: Long,
) : RuntimeException(
    "Task not found: $taskId"
)