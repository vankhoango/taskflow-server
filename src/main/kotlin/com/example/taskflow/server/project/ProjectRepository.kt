package com.example.taskflow.server.project

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<Project, Long> {
    fun findAllByUserId(userId: Long, ): List<Project>

    fun findByIdAndUser_Id(id: Long, userId: Long, ): Project?
}