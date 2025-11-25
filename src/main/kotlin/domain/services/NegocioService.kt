package com.example.domain.services

import com.example.data.tables.repositories.HorarioRepository
import com.example.data.tables.repositories.NegocioRepository
import com.example.data.tables.repositories.ServicioRepository
import com.example.domain.services.models.Horario
import com.example.domain.services.models.HorarioRequest
import com.example.domain.services.models.Negocio
import com.example.domain.services.models.NegocioRequest
import com.example.domain.services.models.Servicio
import com.example.domain.services.models.ServicioRequest

class NegocioService(
    private val negocioRepository: NegocioRepository,
    private val horarioRepository: HorarioRepository,
    private val servicioRepository: ServicioRepository
) {

    suspend fun getAllNegocios() = negocioRepository.getAllNegocios()

    suspend fun getNegocioCompleto(id: Int) = negocioRepository.getNegocioCompleto(id)

    suspend fun createNegocio(request: NegocioRequest): Negocio? {
        require(request.nombreN.isNotBlank()) { "El nombre del negocio no puede estar vacío" }
        require(request.direccion.isNotBlank()) { "La dirección no puede estar vacía" }
        return negocioRepository.createNegocio(request)
    }

    suspend fun updateNegocio(id: Int, request: NegocioRequest) =
        negocioRepository.updateNegocio(id, request)

    suspend fun deleteNegocio(id: Int) = negocioRepository.deleteNegocio(id)

    suspend fun addHorario(request: HorarioRequest): Horario? {
        require(request.dia.isNotBlank()) { "El día no puede estar vacío" }
        return horarioRepository.createHorario(request)
    }

    suspend fun updateHorario(id: Int, request: HorarioRequest) =
        horarioRepository.updateHorario(id, request)

    suspend fun deleteHorario(id: Int) = horarioRepository.deleteHorario(id)

    suspend fun addServicio(request: ServicioRequest): Servicio? {
        require(request.nombre.isNotBlank()) { "El nombre del servicio no puede estar vacío" }
        require(request.precio > 0) { "El precio debe ser mayor a 0" }
        require(request.duracion > 0) { "La duración debe ser mayor a 0" }
        return servicioRepository.createServicio(request)
    }

    suspend fun updateServicio(id: Int, request: ServicioRequest) =
        servicioRepository.updateServicio(id, request)

    suspend fun deleteServicio(id: Int) = servicioRepository.deleteServicio(id)
}