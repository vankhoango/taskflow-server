package com.example.taskflow.server.task

import com.example.taskflow.server.project.ProjectRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/projects/{projectId}/tasks")
class ProjectTasksController(

    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(
        @PathVariable projectId: Long,
        @RequestBody request: CreateTaskRequest,
    ): TaskResponse {
        val project = projectRepository.findById(projectId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Project not found",
                )
            }

        val task = Task(
            title = request.title,
            project = project,
        )

        return taskRepository
            .save(task)
            .toResponse()
    }

    @GetMapping
    fun getTasks(
        @PathVariable projectId: Long,
    ): List<TaskResponse> {
        if (!projectRepository.existsById(projectId)) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Project not found",
            )
        }

        return taskRepository
            .findAllByProjectId(projectId)
            .map { task -> task.toResponse() }
    }

}