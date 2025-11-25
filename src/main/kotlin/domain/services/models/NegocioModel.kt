package com.example.domain.services.models

import kotlinx.serialization.Serializable

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