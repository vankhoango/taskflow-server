package com.example.taskflow.server.auth

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @RequestBody request: RegisterRequest,
    ): UserResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): LoginResponse {
        return authService.login(request)
    }
}