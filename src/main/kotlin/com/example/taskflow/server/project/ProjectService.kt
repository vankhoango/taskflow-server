package com.example.taskflow.server.project

import com.example.taskflow.server.user.UserRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
) {
    fun createProject(
        userId: Long,
        request: CreateProjectRequest,
    ): ProjectResponse {
        val user = userRepository
            .findById(userId)
            .orElseThrow {
                IllegalStateException(
                    "Authenticated user does not exist"
                )
            }

        val project = Project(
            name = request.name,
            user = user,
        )

        return projectRepository
            .save(project)
            .toResponse()
    }

    fun getProject(
        userId: Long,
        projectId: Long,
    ): ProjectResponse {
        return findOwnedProject(
            projectId = projectId,
            userId = userId,
        ).toResponse()
    }

    fun getProjects(
        userId: Long,
    ): List<ProjectResponse> {
        return projectRepository
            .findAllByUserId(userId)
            .map { project ->
                ProjectResponse(
                    id = project.id,
                    name = project.name,
                )
            }
    }

    fun updateProject(
        userId: Long,
        projectId: Long,
        request: UpdateProjectRequest,
    ): ProjectResponse {
        val project = findOwnedProject(
            projectId = projectId,
            userId = userId,
        )

        project.name = request.name

        return projectRepository
            .save(project)
            .toResponse()
    }

    fun deleteProject(
        userId: Long,
        projectId: Long,
    ) {
        val project = findOwnedProject(
            projectId = projectId,
            userId = userId,
        )

        projectRepository.delete(project)
    }

    private fun findOwnedProject(
        projectId: Long,
        userId: Long,
    ): Project {
        return projectRepository.findByIdAndUser_Id(
            id = projectId,
            userId = userId,
        ) ?: throw ProjectNotFoundException(projectId)
    }
}