package com.example.domain.services

import com.example.data.tables.repositories.CitaRepository
import com.example.data.tables.repositories.ServicioRepository
import com.example.domain.services.models.Cita
import com.example.domain.services.models.CitaRequest

class CitaService(
    private val citaRepository: CitaRepository,
    private val servicioRepository: ServicioRepository
) {

    suspend fun getCitasByCliente(clienteId: Int) = citaRepository.getCitasByCliente(clienteId)

    suspend fun getCitasByNegocio(negocioId: Int) = citaRepository.getCitasByNegocio(negocioId)

    suspend fun getCitasPendientesByCliente(clienteId: Int) =
        citaRepository.getCitasPendientesByCliente(clienteId)

    suspend fun createCita(request: CitaRequest): Cita? {
        require(request.asunto.isNotBlank()) { "El asunto no puede estar vacío" }

        val servicio = servicioRepository.getServicioById(request.servicioId)
            ?: throw IllegalArgumentException("Servicio no encontrado")

        return citaRepository.createCita(request, servicio.precio)
    }

    suspend fun updateEstadoCita(id: Int, estado: String): Boolean {
        val estadosValidos = listOf("pendiente", "confirmada", "completada", "cancelada")
        require(estado in estadosValidos) { "Estado inválido" }
        return citaRepository.updateEstadoCita(id, estado)
    }

    suspend fun cancelarCita(id: Int) = citaRepository.cancelarCita(id)
}