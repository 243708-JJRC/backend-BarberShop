package com.example.data.tables.repositories

import com.example.config.DatabaseFactory.dbQuery
import com.example.data.tables.Servicios
import com.example.domain.services.models.Servicio
import com.example.domain.services.models.ServicioRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

interface ServicioRepository {
    suspend fun getServiciosByNegocio(negocioId: Int): List<Servicio>
    suspend fun getServicioById(id: Int): Servicio?
    suspend fun createServicio(request: ServicioRequest): Servicio?
    suspend fun updateServicio(id: Int, request: ServicioRequest): Boolean
    suspend fun deleteServicio(id: Int): Boolean
}

class ServicioRepositoryImpl : ServicioRepository {

    private fun ResultRow.toServicio() = Servicio(
        id = this[Servicios.id],
        nombre = this[Servicios.nombre],
        precio = this[Servicios.precio],
        duracion = this[Servicios.duracion],
        negocioId = this[Servicios.negocioId]
    )

    override suspend fun getServiciosByNegocio(negocioId: Int): List<Servicio> = dbQuery {
        Servicios.select { Servicios.negocioId eq negocioId }.map { it.toServicio() }
    }

    override suspend fun getServicioById(id: Int): Servicio? = dbQuery {
        Servicios.select { Servicios.id eq id }.map { it.toServicio() }.singleOrNull()
    }

    override suspend fun createServicio(request: ServicioRequest): Servicio? = dbQuery {
        val insertStatement = Servicios.insert {
            it[nombre] = request.nombre
            it[precio] = request.precio
            it[duracion] = request.duracion
            it[negocioId] = request.negocioId
        }
        insertStatement.resultedValues?.singleOrNull()?.toServicio()
    }

    override suspend fun updateServicio(id: Int, request: ServicioRequest): Boolean = dbQuery {
        Servicios.update({ Servicios.id eq id }) {
            it[nombre] = request.nombre
            it[precio] = request.precio
            it[duracion] = request.duracion
        } > 0
    }

    override suspend fun deleteServicio(id: Int): Boolean = dbQuery {
        Servicios.deleteWhere { Servicios.id eq id } > 0
    }
}