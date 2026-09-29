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
) {

    private val signingKey: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(encodedSecret)
        Keys.hmacShaKeyFor(keyBytes)
    }

    val accessTokenExpiration: Long
        get() = accessTokenExpirationSeconds

    fun generateAccessToken(user: User): String {
        val now = System.currentTimeMillis()

        return Jwts.builder()
            .subject(user.id.toString())
            .claim("username", user.username)
            .issuedAt(Date(now))
            .expiration(
                Date(
                    now + accessTokenExpirationSeconds * 1_000
                )
            )
            .signWith(signingKey)
            .compact()
    }

    fun extractUserId(token: String): Long {
        val claims = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject.toLong()
    }
}