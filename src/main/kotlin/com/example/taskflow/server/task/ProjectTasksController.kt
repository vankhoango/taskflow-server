package com.example.taskflow.server.task

import com.example.taskflow.server.auth.AuthenticatedUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects/{projectId}/tasks")
class ProjectTasksController(
    private val taskService: TaskService,
) {

    @GetMapping
    fun getTasks(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable projectId: Long,
    ): List<TaskResponse> {
        return taskService.getTasks(
            userId = user.id,
            projectId = projectId,
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable projectId: Long,
        @RequestBody request: CreateTaskRequest,
    ): TaskResponse {
        return taskService.createTask(
            userId = user.id,
            projectId = projectId,
            request = request,
        )
    }
}