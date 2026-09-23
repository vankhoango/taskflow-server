package com.example.taskflow.server.task

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskRepository: TaskRepository,
) {

    @GetMapping("/{taskId}")
    fun getTask(
        @PathVariable taskId: Long,
    ): TaskResponse {
        val task = taskRepository.findById(taskId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found",
                )
            }

        return task.toResponse()
    }

    @PatchMapping("/{taskId}")
    fun updateTask(
        @PathVariable taskId: Long,
        @RequestBody request: UpdateTaskRequest,
    ): TaskResponse {
        val task = taskRepository.findById(taskId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found",
                )
            }

        request.title?.let { title ->
            task.title = title
        }

        request.completed?.let { completed ->
            task.completed = completed
        }

        return taskRepository
            .save(task)
            .toResponse()
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(
        @PathVariable taskId: Long,
    ) {
        val task = taskRepository.findById(taskId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found",
                )
            }

        taskRepository.delete(task)
    }

}