package com.example.domain.services.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

@Serializable
data class AuthResponseCliente(
    val token: String,
    val user: Cliente
)

@Serializable
data class AuthResponseAdmin(
    val token: String,
    val user: Administrador
)

@Serializable
data class TokenClaims(
    val userId: Int,
    val email: String,
    val role: String
)