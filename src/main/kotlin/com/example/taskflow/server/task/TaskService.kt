/*
 * Copyright (c) 2026 Khoa Ngo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.taskflow.server.task

import com.example.taskflow.server.project.ProjectNotFoundException
import com.example.taskflow.server.project.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
) {

    fun getTasks(
        userId: Long,
        projectId: Long,
    ): List<TaskResponse> {
        projectRepository.findByIdAndUser_Id(
            id = projectId,
            userId = userId,
        ) ?: throw ProjectNotFoundException(projectId)

        return taskRepository
            .findAllByProject_IdAndProject_User_Id(
                projectId = projectId,
                userId = userId,
            )
            .map { it.toResponse() }
    }

    fun getTask(
        userId: Long,
        taskId: Long,
    ): TaskResponse {
        return findOwnedTask(
            taskId = taskId,
            userId = userId,
        ).toResponse()
    }

    fun createTask(
        userId: Long,
        projectId: Long,
        request: CreateTaskRequest,
    ): TaskResponse {
        val project = projectRepository.findByIdAndUser_Id(
            id = projectId,
            userId = userId,
        ) ?: throw ProjectNotFoundException(projectId)

        val task = Task(
            title = request.title,
            completed = false,
            project = project,
        )

        return taskRepository
            .save(task)
            .toResponse()
    }

    fun updateTask(
        userId: Long,
        taskId: Long,
        request: UpdateTaskRequest,
    ): TaskResponse {
        val task = findOwnedTask(
            taskId = taskId,
            userId = userId,
        )

        request.title?.let {
            task.title = it
        }

        request.completed?.let {
            task.completed = it
        }

        return taskRepository
            .save(task)
            .toResponse()
    }

    fun deleteTask(
        userId: Long,
        taskId: Long,
    ) {
        val task = findOwnedTask(
            taskId = taskId,
            userId = userId,
        )

        taskRepository.delete(task)
    }

    private fun findOwnedTask(
        taskId: Long,
        userId: Long,
    ): Task {
        return taskRepository
            .findByIdAndProject_User_Id(
                id = taskId,
                userId = userId,
            )
            ?: throw TaskNotFoundException(taskId)
    }

    @Transactional
    fun clearCompletedTasks(
        userId: Long,
        projectId: Long,
    ) {
        projectRepository.findByIdAndUser_Id(
            id = projectId,
            userId = userId,
        ) ?: throw ProjectNotFoundException(projectId)

        taskRepository
            .deleteAllByProject_IdAndProject_User_IdAndCompletedTrue(
                projectId = projectId,
                userId = userId,
            )
    }
}