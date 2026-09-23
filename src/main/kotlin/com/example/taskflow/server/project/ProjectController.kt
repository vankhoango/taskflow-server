package com.example.taskflow.server.project

import org.springframework.http.HttpStatus
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
@RequestMapping("/projects")
class ProjectController (private val projectRepository: ProjectRepository) {

    @GetMapping
    fun getProjects(): List<ProjectResponse> {
        return projectRepository
            .findAll()
            .map { project -> project.toResponse() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(
        @RequestBody request: CreateProjectRequest,
    ): ProjectResponse {
        val project = Project(
            name = request.name,
        )

        return projectRepository
            .save(project)
            .toResponse()
    }

    @GetMapping("/{id}")
    fun getProject(
        @PathVariable id: Long,
    ): ProjectResponse {
        val project = projectRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Project not found",
                )
            }

        return project.toResponse()
    }

    @PatchMapping("/{id}")
    fun updateProject(
        @PathVariable id: Long,
        @RequestBody request: UpdateProjectRequest,
    ): ProjectResponse {
        val project = projectRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Project not found",
                )
            }

        project.name = request.name

        return projectRepository
            .save(project)
            .toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProject(
        @PathVariable id: Long,
    ) {
        val project = projectRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Project not found",
                )
            }
        projectRepository.delete(project)
    }
}