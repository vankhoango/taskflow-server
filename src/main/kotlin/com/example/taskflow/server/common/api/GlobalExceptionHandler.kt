package com.example.taskflow.server.common.api

import com.example.taskflow.server.auth.exception.InvalidCredentialsException
import com.example.taskflow.server.auth.exception.InvalidRegistrationException
import com.example.taskflow.server.auth.exception.UsernameAlreadyExistsException
import com.example.taskflow.server.project.ProjectNotFoundException
import com.example.taskflow.server.task.TaskNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(
        exception: InvalidCredentialsException,
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ApiErrorResponse(
                    code = "INVALID_CREDENTIALS",
                    message = exception.message
                        ?: "Invalid username or password",
                )
            )
    }

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExists(
        exception: UsernameAlreadyExistsException,
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ApiErrorResponse(
                    code = "USERNAME_ALREADY_EXISTS",
                    message = exception.message
                        ?: "Username already exists",
                )
            )
    }

    @ExceptionHandler(InvalidRegistrationException::class)
    fun handleInvalidRegistration(
        exception: InvalidRegistrationException,
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiErrorResponse(
                    code = "INVALID_REGISTRATION",
                    message = exception.message
                        ?: "Invalid registration",
                )
            )
    }

    @ExceptionHandler(ProjectNotFoundException::class)
    fun handleProjectNotFound(
        exception: ProjectNotFoundException,
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ApiErrorResponse(
                    code = "PROJECT_NOT_FOUND",
                    message = exception.message
                        ?: "Project not found",
                )
            )
    }

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFound(
        exception: TaskNotFoundException,
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ApiErrorResponse(
                    code = "TASK_NOT_FOUND",
                    message = exception.message
                        ?: "Task not found",
                )
            )
    }
}