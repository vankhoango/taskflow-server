package com.example.taskflow.server.auth.token

import com.example.taskflow.server.auth.AuthenticatedUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeader =
            request.getHeader("Authorization")

        if (
            authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.removePrefix("Bearer ").trim()

        try {
            val userId = jwtService.extractUserId(token)

            val authenticatedUser = AuthenticatedUser(
                id = userId,
            )

            val authentication =
                UsernamePasswordAuthenticationToken(
                    authenticatedUser,
                    null,
                    emptyList(),
                )

            SecurityContextHolder
                .getContext()
                .authentication = authentication
        } catch (_: Exception) {
            // Invalid token.
            // Leave the request unauthenticated.
        }

        filterChain.doFilter(request, response)
    }
}