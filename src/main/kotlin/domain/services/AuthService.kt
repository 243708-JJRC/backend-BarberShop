package com.example.domain.services

import com.example.data.tables.repositories.AdministradorRepository
import com.example.data.tables.repositories.ClienteRepository
import com.example.domain.services.models.Administrador
import com.example.domain.services.models.AdministradorLogin
import com.example.domain.services.models.AdministradorRequest
import com.example.domain.services.models.Cliente
import com.example.domain.services.models.ClienteLogin
import com.example.domain.services.models.ClienteRequest

class AuthService(
    private val clienteRepository: ClienteRepository,
    private val administradorRepository: AdministradorRepository
) {

    // Login Cliente
    suspend fun loginCliente(request: ClienteLogin): Cliente? {
        return clienteRepository.login(request.email, request.contraseña)
    }

    // Registro Cliente
    suspend fun registerCliente(request: ClienteRequest): Cliente? {
        // Verificar email único
        val existingCliente = clienteRepository.getClienteByEmail(request.email)
        if (existingCliente != null) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        validateClienteRequest(request)
        return clienteRepository.createCliente(request)
    }

    // Login Administrador
    suspend fun loginAdministrador(request: AdministradorLogin): Administrador? {
        return administradorRepository.login(request.email, request.contraseña)
    }

    // Registro Administrador
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
        require(request.telefono.length == 10) { "El teléfono debe tener 10 dígitos" }
        require(request.direccion.isNotBlank()) { "La dirección no puede estar vacía" }
    }

    private fun validateAdministradorRequest(request: AdministradorRequest) {
        require(request.nombres.isNotBlank()) { "El nombre no puede estar vacío" }
        require(request.apellidoP.isNotBlank()) { "El apellido paterno no puede estar vacío" }
        require(request.apellidoM.isNotBlank()) { "El apellido materno no puede estar vacío" }
        require(request.email.isNotBlank() && request.email.contains("@")) { "Email inválido" }
        require(request.contraseña.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }
        require(request.telefono.length == 10) { "El teléfono debe tener 10 dígitos" }
    }
}