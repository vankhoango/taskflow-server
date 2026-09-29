package com.example.taskflow.server.auth

import com.example.taskflow.server.auth.token.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class AuthConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }

            .sessionManagement {
                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }

            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/auth/register",
                        "/auth/login",
                        "/health",
                    )
                    .permitAll()

                    .anyRequest()
                    .authenticated()
            }

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }
}