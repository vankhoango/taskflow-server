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