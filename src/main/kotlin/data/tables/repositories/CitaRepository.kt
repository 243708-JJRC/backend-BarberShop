package com.example.data.tables.repositories

import com.example.config.DatabaseFactory.dbQuery
import com.example.data.tables.Citas
import com.example.domain.services.models.Cita
import com.example.domain.services.models.CitaCompleta
import com.example.domain.services.models.CitaRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDate

interface CitaRepository {
    suspend fun getAllCitas(): List<Cita>
    suspend fun getCitaById(id: Int): Cita?
    suspend fun getCitasByCliente(clienteId: Int): List<CitaCompleta>
    suspend fun getCitasByNegocio(negocioId: Int): List<CitaCompleta>
    suspend fun getCitasPendientesByCliente(clienteId: Int): List<CitaCompleta>
    suspend fun createCita(request: CitaRequest, precio: Float): Cita?
    suspend fun updateEstadoCita(id: Int, estado: String): Boolean
    suspend fun cancelarCita(id: Int): Boolean
}

class CitaRepositoryImpl(
    private val clienteRepository: ClienteRepository,
    private val negocioRepository: NegocioRepository,
    private val servicioRepository: ServicioRepository
) : CitaRepository {

    private fun ResultRow.toCita() = Cita(
        id = this[Citas.id],
        fechaRealizacion = this[Citas.fechaRealizacion],
        fechaCita = this[Citas.fechaCita],
        precio = this[Citas.precio],
        asunto = this[Citas.asunto],
        estado = this[Citas.estado],
        clienteId = this[Citas.clienteId],
        negocioId = this[Citas.negocioId],
        servicioId = this[Citas.servicioId]
    )

    override suspend fun getAllCitas(): List<Cita> = dbQuery {
        Citas.selectAll().map { it.toCita() }
    }

    override suspend fun getCitaById(id: Int): Cita? = dbQuery {
        Citas.select { Citas.id eq id }.map { it.toCita() }.singleOrNull()
    }

    override suspend fun getCitasByCliente(clienteId: Int): List<CitaCompleta> {
        val citas = dbQuery {
            Citas.select { Citas.clienteId eq clienteId }
                .orderBy(Citas.fechaCita to SortOrder.DESC)
                .map { it.toCita() }
        }
        return citas.mapNotNull { buildCitaCompleta(it) }
    }

    override suspend fun getCitasByNegocio(negocioId: Int): List<CitaCompleta> {
        val citas = dbQuery {
            Citas.select { Citas.negocioId eq negocioId }
                .orderBy(Citas.fechaCita to SortOrder.DESC)
                .map { it.toCita() }
        }
        return citas.mapNotNull { buildCitaCompleta(it) }
    }

    override suspend fun getCitasPendientesByCliente(clienteId: Int): List<CitaCompleta> {
        val citas = dbQuery {
            Citas.select {
                (Citas.clienteId eq clienteId) and (Citas.estado eq "pendiente")
            }
                .orderBy(Citas.fechaCita to SortOrder.ASC)
                .map { it.toCita() }
        }
        return citas.mapNotNull { buildCitaCompleta(it) }
    }

    override suspend fun createCita(request: CitaRequest, precio: Float): Cita? = dbQuery {
        val insertStatement = Citas.insert {
            it[fechaRealizacion] = LocalDate.now()
            it[fechaCita] = request.fechaCita
            it[Citas.precio] = precio
            it[asunto] = request.asunto
            it[estado] = "pendiente"
            it[clienteId] = request.clienteId
            it[negocioId] = request.negocioId
            it[servicioId] = request.servicioId
        }
        insertStatement.resultedValues?.singleOrNull()?.toCita()
    }

    override suspend fun updateEstadoCita(id: Int, estado: String): Boolean = dbQuery {
        Citas.update({ Citas.id eq id }) {
            it[Citas.estado] = estado
        } > 0
    }

    override suspend fun cancelarCita(id: Int): Boolean = dbQuery {
        Citas.update({ Citas.id eq id }) {
            it[estado] = "cancelada"
        } > 0
    }

    private suspend fun buildCitaCompleta(cita: Cita): CitaCompleta? {
        val cliente = clienteRepository.getClienteById(cita.clienteId) ?: return null
        val negocio = negocioRepository.getNegocioById(cita.negocioId) ?: return null
        val servicio = servicioRepository.getServicioById(cita.servicioId) ?: return null
        return CitaCompleta(cita, cliente, negocio, servicio)
    }
}