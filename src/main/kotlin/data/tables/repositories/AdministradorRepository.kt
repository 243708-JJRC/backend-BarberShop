package com.example.data.tables.repositories


import com.example.config.DatabaseFactory.dbQuery
import com.example.data.tables.Administradores
import com.example.domain.services.models.Administrador
import com.example.domain.services.models.AdministradorRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.mindrot.jbcrypt.BCrypt

interface AdministradorRepository {
    suspend fun getAllAdministradores(): List<Administrador>
    suspend fun getAdministradorById(id: Int): Administrador?
    suspend fun getAdministradorByEmail(email: String): Pair<Administrador, String>?
    suspend fun createAdministrador(request: AdministradorRequest): Administrador?
    suspend fun updateAdministrador(id: Int, request: AdministradorRequest): Boolean
    suspend fun deleteAdministrador(id: Int): Boolean
}

class AdministradorRepositoryImpl : AdministradorRepository {

    private fun ResultRow.toAdministrador() = Administrador(
        id = this[Administradores.id],
        nombres = this[Administradores.nombres],
        apellidoP = this[Administradores.apellidoP],
        apellidoM = this[Administradores.apellidoM],
        telefono = this[Administradores.telefono],
        email = this[Administradores.email],
        negocioId = this[Administradores.negocioId]
    )

    override suspend fun getAllAdministradores(): List<Administrador> = dbQuery {
        Administradores.selectAll().map { it.toAdministrador() }
    }

    override suspend fun getAdministradorById(id: Int): Administrador? = dbQuery {
        Administradores.select { Administradores.id eq id }
            .map { it.toAdministrador() }
            .singleOrNull()
    }

    override suspend fun getAdministradorByEmail(email: String): Pair<Administrador, String>? = dbQuery {
        Administradores.select { Administradores.email eq email }
            .map { it.toAdministrador() to it[Administradores.contraseña] }
            .singleOrNull()
    }

    override suspend fun createAdministrador(request: AdministradorRequest): Administrador? = dbQuery {
        val hashedPassword = BCrypt.hashpw(request.contraseña, BCrypt.gensalt())
        val insertStatement = Administradores.insert {
            it[nombres] = request.nombres
            it[apellidoP] = request.apellidoP
            it[apellidoM] = request.apellidoM
            it[telefono] = request.telefono
            it[email] = request.email
            it[contraseña] = hashedPassword
            it[negocioId] = request.negocioId
        }
        insertStatement.resultedValues?.singleOrNull()?.toAdministrador()
    }

    override suspend fun updateAdministrador(id: Int, request: AdministradorRequest): Boolean = dbQuery {
        Administradores.update({ Administradores.id eq id }) {
            it[nombres] = request.nombres
            it[apellidoP] = request.apellidoP
            it[apellidoM] = request.apellidoM
            it[telefono] = request.telefono
            it[email] = request.email
            it[negocioId] = request.negocioId
        } > 0
    }

    override suspend fun deleteAdministrador(id: Int): Boolean = dbQuery {
        Administradores.deleteWhere { Administradores.id eq id } > 0
    }
}