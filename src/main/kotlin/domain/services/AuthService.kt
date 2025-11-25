package com.example.domain.services

import com.example.data.tables.repositories.AdministradorRepository
import com.example.data.tables.repositories.ClienteRepository
import com.example.domain.services.models.Administrador
import com.example.domain.services.models.AdministradorLogin
import com.example.domain.services.models.AdministradorRequest
import com.example.domain.services.models.AuthResponseAdmin
import com.example.domain.services.models.AuthResponseCliente
import com.example.domain.services.models.Cliente
import com.example.domain.services.models.ClienteLogin
import com.example.domain.services.models.ClienteRequest
import com.example.utils.JwtConfig
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val clienteRepository: ClienteRepository,
    private val administradorRepository: AdministradorRepository
) {
    suspend fun loginCliente(request: ClienteLogin): AuthResponseCliente? {
        val (cliente, hashedPassword) = clienteRepository.getClienteByEmail(request.email) ?: return null

        if (!BCrypt.checkpw(request.contraseña, hashedPassword)) {
            return null
        }

        val token = JwtConfig.generateToken(cliente.id, cliente.email, "cliente")
        return AuthResponseCliente(token, cliente)
    }
    suspend fun loginAdministrador(request: AdministradorLogin): AuthResponseAdmin? {
        val (admin, hashedPassword) = administradorRepository.getAdministradorByEmail(request.email) ?: return null

        if (!BCrypt.checkpw(request.contraseña, hashedPassword)) {
            return null
        }

        val token = JwtConfig.generateToken(admin.id, admin.email, "administrador")
        return AuthResponseAdmin(token, admin)
    }

    suspend fun registerCliente(request: ClienteRequest): Cliente? {
        val existingCliente = clienteRepository.getClienteByEmail(request.email)
        if (existingCliente != null) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        validateClienteRequest(request)
        return clienteRepository.createCliente(request)
    }

    suspend fun registerAdministrador(request: AdministradorRequest): Administrador? {
        val existingAdmin = administradorRepository.getAdministradorByEmail(request.email)
        if (existingAdmin != null) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        validateAdministradorRequest(request)
        return administradorRepository.createAdministrador(request)
    }

    private fun validateClienteRequest(request: ClienteRequest) {
        require(request.nombres.isNotBlank()) { "El nombre no puede estar vacío" }
        require(request.apellidoP.isNotBlank()) { "El apellido paterno no puede estar vacío" }
        require(request.apellidoM.isNotBlank()) { "El apellido materno no puede estar vacío" }
        require(request.email.isNotBlank() && request.email.contains("@")) { "Email inválido" }
        require(request.contraseña.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }
        require(request.telefono > 0) { "Teléfono inválido" }
        require(request.direccion.isNotBlank()) { "La dirección no puede estar vacía" }
    }

    private fun validateAdministradorRequest(request: AdministradorRequest) {
        require(request.nombres.isNotBlank()) { "El nombre no puede estar vacío" }
        require(request.apellidoP.isNotBlank()) { "El apellido paterno no puede estar vacío" }
        require(request.apellidoM.isNotBlank()) { "El apellido materno no puede estar vacío" }
        require(request.email.isNotBlank() && request.email.contains("@")) { "Email inválido" }
        require(request.contraseña.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }
        require(request.telefono > 0) { "Teléfono inválido" }
    }
}