package com.example.taskflow.server.project

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


@RestController
@RequestMapping("/projects")
class ProjectController(
    private val projectService: ProjectService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @RequestBody request: CreateProjectRequest,
    ): ProjectResponse {
        return projectService.createProject(
            userId = user.id,
            request = request,
        )
    }

    @GetMapping
    fun getProjects(
        @AuthenticationPrincipal user: AuthenticatedUser,
    ): List<ProjectResponse> {
        return projectService.getProjects(
            userId = user.id,
        )
    }

    @GetMapping("/{id}")
    fun getProject(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable id: Long,
    ): ProjectResponse {
        return projectService.getProject(
            userId = user.id,
            projectId = id,
        )
    }

    @PatchMapping("/{id}")
    fun updateProject(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable id: Long,
        @RequestBody request: UpdateProjectRequest,
    ): ProjectResponse {
        return projectService.updateProject(
            userId = user.id,
            projectId = id,
            request = request,
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProject(
        @AuthenticationPrincipal user: AuthenticatedUser,
        @PathVariable id: Long,
    ) {
        projectService.deleteProject(
            userId = user.id,
            projectId = id,
        )
    }
}