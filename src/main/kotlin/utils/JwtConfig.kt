package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

object JwtConfig {
    private val secret = System.getenv("JWT_SECRET") ?: "your-secret-key-change-in-production"
    private val issuer = "barbershop-api"
    private val validityInMs = 36_000_00 * 24 * 30
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: Int, email: String, role: String): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(algorithm)
    }

    fun verifyToken(token: String): DecodedJWT {
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
        return verifier.verify(token)
    }
}