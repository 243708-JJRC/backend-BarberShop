package com.example.domain.services.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.json.JsonElement
import java.time.LocalDate
import java.time.LocalTime

// Cliente
@Serializable
data class Cliente(
    val id: Int = 0,
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: Int,
    val email: String,
    val direccion: String
)

@Serializable
data class ClienteRequest(
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: Int,
    val email: String,
    val contraseña: String,
    val direccion: String
)

@Serializable
data class ClienteLogin(
    val email: String,
    val contraseña: String
)

// Administrador
@Serializable
data class Administrador(
    val id: Int = 0,
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: Int,
    val email: String,
    val negocioId: Int? = null
)

@Serializable
data class AdministradorRequest(
    val nombres: String,
    val apellidoP: String,
    val apellidoM: String,
    val telefono: Int,
    val email: String,
    val contraseña: String,
    val negocioId: Int? = null
)

@Serializable
data class AdministradorLogin(
    val email: String,
    val contraseña: String
)

// Negocio
@Serializable
data class Negocio(
    val id: Int = 0,
    val nombreN: String,
    val direccion: String
)

@Serializable
data class NegocioRequest(
    val nombreN: String,
    val direccion: String
)

@Serializable
data class NegocioCompleto(
    val negocio: Negocio,
    val horarios: List<Horario>,
    val servicios: List<Servicio>
)

// Horario
@Serializable
data class Horario(
    val id: Int = 0,
    val dia: String,
    @Contextual val horaApertura: LocalTime? = null,
    @Contextual val horaCierre: LocalTime? = null,
    val negocioId: Int
)

@Serializable
data class HorarioRequest(
    val dia: String,
    @Contextual val horaApertura: LocalTime? = null,
    @Contextual val horaCierre: LocalTime? = null,
    val negocioId: Int
)

// Servicio
@Serializable
data class Servicio(
    val id: Int = 0,
    val nombre: String,
    val precio: Float,
    val duracion: Int,
    val negocioId: Int
)

@Serializable
data class ServicioRequest(
    val nombre: String,
    val precio: Float,
    val duracion: Int,
    val negocioId: Int
)

// Cita
@Serializable
data class Cita(
    val id: Int = 0,
    @Contextual val fechaRealizacion: LocalDate? = null,
    @Contextual val fechaCita: LocalDate,
    val precio: Float,
    val asunto: String,
    val estado: String,
    val clienteId: Int,
    val negocioId: Int,
    val servicioId: Int
)

@Serializable
data class CitaRequest(
    @Contextual val fechaCita: LocalDate,
    val asunto: String,
    val clienteId: Int,
    val negocioId: Int,
    val servicioId: Int
)

@Serializable
data class CitaCompleta(
    val cita: Cita,
    val cliente: Cliente,
    val negocio: Negocio,
    val servicio: Servicio
)

@Serializable
data class ActualizarEstadoCita(
    val estado: String
)

// Respuestas
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