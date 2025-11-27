package com.example.domain.services.models

import kotlinx.serialization.Serializable

@Serializable
data class Administrador(
    val id: Int = 0,
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: String,
    val email: String,
    val negocioId: Int? = null
)

@Serializable
data class AdministradorRequest(
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: String,
    val email: String,
    val contraseña: String,
    val negocioId: Int? = null
)

@Serializable
data class AdministradorLogin(
    val email: String,
    val contraseña: String
)

@Serializable
data class LoginAdminResponse(
    val success: Boolean,
    val message: String,
    val administrador: Administrador? = null
)