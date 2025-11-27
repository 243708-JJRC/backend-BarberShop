package com.example.domain.services.models

import kotlinx.serialization.Serializable

@Serializable
data class Cliente(
    val id: Int = 0,
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: String,
    val email: String,
    val direccion: String
)

@Serializable
data class ClienteRequest(
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: String,
    val email: String,
    val contraseña: String,
    val direccion: String
)

@Serializable
data class ClienteLogin(
    val email: String,
    val contraseña: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val cliente: Cliente? = null
)