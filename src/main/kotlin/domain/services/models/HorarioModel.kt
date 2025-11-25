package com.example.domain.services.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime

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