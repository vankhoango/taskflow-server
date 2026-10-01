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

package com.example.taskflow.server.auth.token

import com.example.taskflow.server.auth.AuthenticatedUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {

    private val logger =
        LoggerFactory.getLogger(
            JwtAuthenticationFilter::class.java
        )

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

        val token =
            authorizationHeader
                .removePrefix("Bearer ")
                .trim()

        try {
            val userId =
                jwtService.extractAccessTokenUserId(
                    token
                )

            val authenticatedUser =
                AuthenticatedUser(
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

        } catch (exception: Exception) {

            logger.warn(
                "JWT rejected path={}, tokenHash={}, " +
                        "exception={}, message={}",
                request.requestURI,
                token.hashCode(),
                exception::class.simpleName,
                exception.message,
            )
        }

        filterChain.doFilter(
            request,
            response,
        )

    }
}