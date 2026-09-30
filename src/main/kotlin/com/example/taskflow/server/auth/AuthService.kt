package com.example.taskflow.server.auth

import com.example.taskflow.server.auth.exception.InvalidCredentialsException
import com.example.taskflow.server.auth.exception.InvalidRegistrationException
import com.example.taskflow.server.auth.exception.UsernameAlreadyExistsException
import com.example.taskflow.server.auth.token.JwtService
import com.example.taskflow.server.user.User
import com.example.taskflow.server.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {

    fun register(request: RegisterRequest): UserResponse {
        val username = request.username.trim()

        if (username.isBlank()) {
            throw InvalidRegistrationException(
                "Username must not be blank"
            )
        }

        if (request.password.isBlank()) {
            throw InvalidRegistrationException(
                "Password must not be blank"
            )
        }

        if (userRepository.existsByUsername(username)) {
            throw UsernameAlreadyExistsException(username)
        }

        val passwordHash = requireNotNull(passwordEncoder.encode(request.password)) {
            "Password encoder returned null"
        }

        val user = User(
            username = username,
            passwordHash = passwordHash
        )

        val savedUser = userRepository.save(user)

        return UserResponse(
            id = savedUser.id,
            username = savedUser.username,
        )
    }

    fun login(request: LoginRequest): LoginResponse {
        val username = request.username.trim()

        val user = userRepository.findByUsername(username)
            ?: throw InvalidCredentialsException()

        val passwordMatches = passwordEncoder.matches(
            request.password,
            user.passwordHash,
        )

        if (!passwordMatches) {
            throw InvalidCredentialsException()
        }

        val accessToken = jwtService.generateAccessToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = "Bearer",
            expiresIn =
                jwtService.accessTokenExpiration,
        )
    }

    fun refresh(
        refreshToken: String,
    ): RefreshTokenResponse {
        val userId =
            jwtService.extractRefreshTokenUserId(
                refreshToken
            )

        val user = userRepository.findById(userId).orElseThrow {
            InvalidCredentialsException()
                }

        val accessToken =
            jwtService.generateAccessToken(user)

        return RefreshTokenResponse(
            accessToken = accessToken,
            expiresIn =
                jwtService.accessTokenExpiration,
        )
    }
}