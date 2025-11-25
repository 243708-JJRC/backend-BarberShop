package com.example.domain.services.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

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
