package com.example.taskflow.server.task

import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {
    fun findAllByProjectId(projectId: Long): List<Task>
}