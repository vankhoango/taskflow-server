package com.example.taskflow.server.task

import com.example.taskflow.server.auth.AuthenticatedUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
) {

    @GetMapping("/projects/{projectId}/tasks")
    fun getTasks(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable projectId: Long,
    ): List<TaskResponse> {
        return taskService.getTasks(
            userId = user.id,
            projectId = projectId,
        )
    }

    @GetMapping("/tasks/{id}")
    fun getTask(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable id: Long,
    ): TaskResponse {
        return taskService.getTask(
            userId = user.id,
            taskId = id,
        )
    }

    @PostMapping("/projects/{projectId}/tasks")
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

    @PatchMapping("/tasks/{id}")
    fun updateTask(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable id: Long,
        @RequestBody request: UpdateTaskRequest,
    ): TaskResponse {
        return taskService.updateTask(
            userId = user.id,
            taskId = id,
            request = request,
        )
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable id: Long,
    ) {
        taskService.deleteTask(
            userId = user.id,
            taskId = id,
        )
    }

}