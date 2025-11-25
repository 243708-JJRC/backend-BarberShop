package com.example.data.tables.repositories

import com.example.config.DatabaseFactory.dbQuery
import com.example.data.tables.Horarios
import com.example.domain.services.models.Horario
import com.example.domain.services.models.HorarioRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

interface HorarioRepository {
    suspend fun getHorariosByNegocio(negocioId: Int): List<Horario>
    suspend fun getHorarioById(id: Int): Horario?
    suspend fun createHorario(request: HorarioRequest): Horario?
    suspend fun updateHorario(id: Int, request: HorarioRequest): Boolean
    suspend fun deleteHorario(id: Int): Boolean
}

class HorarioRepositoryImpl : HorarioRepository {

    private fun ResultRow.toHorario() = Horario(
        id = this[Horarios.id],
        dia = this[Horarios.dia],
        horaApertura = this[Horarios.horaApertura],
        horaCierre = this[Horarios.horaCierre],
        negocioId = this[Horarios.negocioId]
    )

    override suspend fun getHorariosByNegocio(negocioId: Int): List<Horario> = dbQuery {
        Horarios.select { Horarios.negocioId eq negocioId }.map { it.toHorario() }
    }

    override suspend fun getHorarioById(id: Int): Horario? = dbQuery {
        Horarios.select { Horarios.id eq id }.map { it.toHorario() }.singleOrNull()
    }

    override suspend fun createHorario(request: HorarioRequest): Horario? = dbQuery {
        val insertStatement = Horarios.insert {
            it[dia] = request.dia
            it[horaApertura] = request.horaApertura
            it[horaCierre] = request.horaCierre
            it[negocioId] = request.negocioId
        }
        insertStatement.resultedValues?.singleOrNull()?.toHorario()
    }

    override suspend fun updateHorario(id: Int, request: HorarioRequest): Boolean = dbQuery {
        Horarios.update({ Horarios.id eq id }) {
            it[dia] = request.dia
            it[horaApertura] = request.horaApertura
            it[horaCierre] = request.horaCierre
        } > 0
    }

    override suspend fun deleteHorario(id: Int): Boolean = dbQuery {
        Horarios.deleteWhere { Horarios.id eq id } > 0
    }
}