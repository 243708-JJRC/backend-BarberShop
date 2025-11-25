package com.example.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.domain.services.models.TokenClaims
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    val secret = System.getenv("JWT_SECRET") ?: "your-secret-key-change-in-production"
    val issuer = "barbershop-api"
    val algorithm = Algorithm.HMAC256(secret)

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
            )

            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val email = credential.payload.getClaim("email").asString()
                val role = credential.payload.getClaim("role").asString()

                if (userId != null && email != null && role != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

// Extension para obtener claims del token
fun JWTPrincipal.getTokenClaims(): TokenClaims {
    return TokenClaims(
        userId = payload.getClaim("userId").asInt(),
        email = payload.getClaim("email").asString(),
        role = payload.getClaim("role").asString()
    )
}