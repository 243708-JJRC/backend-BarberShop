package com.example.data.tables.repositories

import com.example.config.DatabaseFactory.dbQuery
import com.example.data.tables.Negocios
import com.example.domain.services.models.Negocio
import com.example.domain.services.models.NegocioCompleto
import com.example.domain.services.models.NegocioRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface NegocioRepository {
    suspend fun getAllNegocios(): List<Negocio>
    suspend fun getNegocioById(id: Int): Negocio?
    suspend fun getNegocioCompleto(id: Int): NegocioCompleto?
    suspend fun createNegocio(request: NegocioRequest): Negocio?
    suspend fun updateNegocio(id: Int, request: NegocioRequest): Boolean
    suspend fun deleteNegocio(id: Int): Boolean
}

class NegocioRepositoryImpl(
    private val horarioRepository: HorarioRepository,
    private val servicioRepository: ServicioRepository
) : NegocioRepository {

    private fun ResultRow.toNegocio() = Negocio(
        id = this[Negocios.id],
        nombreN = this[Negocios.nombreN],
        direccion = this[Negocios.direccion]
    )

    override suspend fun getAllNegocios(): List<Negocio> = dbQuery {
        Negocios.selectAll().map { it.toNegocio() }
    }

    override suspend fun getNegocioById(id: Int): Negocio? = dbQuery {
        Negocios.select { Negocios.id eq id }.map { it.toNegocio() }.singleOrNull()
    }

    override suspend fun getNegocioCompleto(id: Int): NegocioCompleto? {
        val negocio = getNegocioById(id) ?: return null
        val horarios = horarioRepository.getHorariosByNegocio(id)
        val servicios = servicioRepository.getServiciosByNegocio(id)
        return NegocioCompleto(negocio, horarios, servicios)
    }

    override suspend fun createNegocio(request: NegocioRequest): Negocio? = dbQuery {
        val insertStatement = Negocios.insert {
            it[nombreN] = request.nombreN
            it[direccion] = request.direccion
        }
        insertStatement.resultedValues?.singleOrNull()?.toNegocio()
    }

    override suspend fun updateNegocio(id: Int, request: NegocioRequest): Boolean = dbQuery {
        Negocios.update({ Negocios.id eq id }) {
            it[nombreN] = request.nombreN
            it[direccion] = request.direccion
        } > 0
    }

    override suspend fun deleteNegocio(id: Int): Boolean = dbQuery {
        Negocios.deleteWhere { Negocios.id eq id } > 0
    }
}