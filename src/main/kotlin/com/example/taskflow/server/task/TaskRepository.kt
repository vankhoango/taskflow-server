package com.example.taskflow.server.task

import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {

    fun findAllByProject_IdAndProject_User_Id(
        projectId: Long,
        userId: Long,
    ): List<Task>

    fun findByIdAndProject_User_Id(
        id: Long,
        userId: Long,
    ): Task?

    fun deleteAllByProject_IdAndProject_User_IdAndCompletedTrue(
        projectId: Long,
        userId: Long,
    )
}