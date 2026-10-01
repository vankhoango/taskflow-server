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

import com.example.taskflow.server.user.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${auth.jwt.secret}")
    private val encodedSecret: String,

    @Value("\${auth.jwt.access-token-expiration}")
    private val accessTokenExpirationSeconds: Long,

    @Value("\${auth.jwt.refresh-token-expiration}")
    private val refreshTokenExpirationSeconds: Long,
) {

    private enum class TokenType(
        val claimValue: String,
    ) {
        ACCESS("access"),
        REFRESH("refresh"),
    }

    private fun generateToken(
        user: User,
        tokenType: TokenType,
        expirationSeconds: Long,
    ): String {
        val now =
            System.currentTimeMillis()

        return Jwts.builder()
            .subject(
                user.id.toString()
            )
            .claim(
                "username",
                user.username,
            )
            .claim(
                "type",
                tokenType.claimValue,
            )
            .issuedAt(
                Date(now)
            )
            .expiration(
                Date(
                    now + expirationSeconds * 1_000
                )
            )
            .signWith(signingKey)
            .compact()
    }

    private val signingKey: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(encodedSecret)
        Keys.hmacShaKeyFor(keyBytes)
    }

    val accessTokenExpiration: Long
        get() = accessTokenExpirationSeconds

    fun generateAccessToken(
        user: User,
    ): String {
        return generateToken(
            user = user,
            tokenType = TokenType.ACCESS,
            expirationSeconds =
                accessTokenExpirationSeconds,
        )
    }

    fun generateRefreshToken(
        user: User,
    ): String {
        return generateToken(
            user = user,
            tokenType = TokenType.REFRESH,
            expirationSeconds =
                refreshTokenExpirationSeconds,
        )
    }

    fun extractAccessTokenUserId(
        token: String,
    ): Long {
        val claims =
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload

        require(
            claims["type"] == TokenType.ACCESS.claimValue
        ) {
            "Token is not an access token"
        }

        return claims.subject.toLong()
    }

    fun extractRefreshTokenUserId(
        token: String,
    ): Long {
        val claims =
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload

        require(
            claims["type"] ==
                    TokenType.REFRESH.claimValue
        ) {
            "Token is not a refresh token"
        }

        return claims.subject.toLong()
    }
}