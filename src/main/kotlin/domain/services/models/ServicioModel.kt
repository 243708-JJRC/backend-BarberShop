package com.example.domain.services.models

import kotlinx.serialization.Serializable

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