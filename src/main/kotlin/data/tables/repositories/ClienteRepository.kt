package com.example.data.tables.repositories

import com.example.config.DatabaseFactory.dbQuery
import com.example.data.tables.Clientes
import com.example.domain.services.models.Cliente
import com.example.domain.services.models.ClienteRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface ClienteRepository {
    suspend fun getAllClientes(): List<Cliente>
    suspend fun getClienteById(id: Int): Cliente?
    suspend fun getClienteByEmail(email: String): Cliente?
    suspend fun createCliente(request: ClienteRequest): Cliente?
    suspend fun updateCliente(id: Int, request: ClienteRequest): Boolean
    suspend fun deleteCliente(id: Int): Boolean
    suspend fun login(email: String, contraseña: String): Cliente?
}

class ClienteRepositoryImpl : ClienteRepository {

    private fun ResultRow.toCliente() = Cliente(
        id = this[Clientes.id],
        nombres = this[Clientes.nombres],
        apellidoP = this[Clientes.apellidoP],
        apellidoM = this[Clientes.apellidoM],
        telefono = this[Clientes.telefono],
        email = this[Clientes.email],
        direccion = this[Clientes.direccion]
    )

    override suspend fun getAllClientes(): List<Cliente> = dbQuery {
        Clientes.selectAll().map { it.toCliente() }
    }

    override suspend fun getClienteById(id: Int): Cliente? = dbQuery {
        Clientes.select { Clientes.id eq id }.map { it.toCliente() }.singleOrNull()
    }

    override suspend fun getClienteByEmail(email: String): Cliente? = dbQuery {
        Clientes.select { Clientes.email eq email }.map { it.toCliente() }.singleOrNull()
    }

    override suspend fun createCliente(request: ClienteRequest): Cliente? = dbQuery {
        val insertStatement = Clientes.insert {
            it[nombres] = request.nombres
            it[apellidoP] = request.apellidoP
            it[apellidoM] = request.apellidoM
            it[telefono] = request.telefono
            it[email] = request.email
            it[contraseña] = request.contraseña
            it[direccion] = request.direccion
        }
        insertStatement.resultedValues?.singleOrNull()?.toCliente()
    }

    override suspend fun updateCliente(id: Int, request: ClienteRequest): Boolean = dbQuery {
        Clientes.update({ Clientes.id eq id }) {
            it[nombres] = request.nombres
            it[apellidoP] = request.apellidoP
            it[apellidoM] = request.apellidoM
            it[telefono] = request.telefono
            it[email] = request.email
            it[contraseña] = request.contraseña
            it[direccion] = request.direccion
        } > 0
    }

    override suspend fun deleteCliente(id: Int): Boolean = dbQuery {
        Clientes.deleteWhere { Clientes.id eq id } > 0
    }

    override suspend fun login(email: String, contraseña: String): Cliente? = dbQuery {
        Clientes.select {
            (Clientes.email eq email) and (Clientes.contraseña eq contraseña)
        }.map { it.toCliente() }.singleOrNull()
    }
}