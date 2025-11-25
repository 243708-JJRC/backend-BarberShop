package com.example.routes

import com.example.domain.services.AuthService
import com.example.domain.services.models.AdministradorLogin
import com.example.domain.services.models.AdministradorRequest
import com.example.domain.services.models.ApiResponse
import com.example.domain.services.models.ClienteLogin
import com.example.domain.services.models.ClienteRequest
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.receive

fun Route.authRoutes(authService: AuthService) {
    route("/auth") {

        // Registro de cliente
        post("/cliente/register") {
            try {
                val request = call.receive<ClienteRequest>()
                val cliente = authService.registerCliente(request)
                if (cliente != null) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Cliente registrado exitosamente", cliente)
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ApiResponse<Any>(false, "Error al registrar cliente")
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Any>(false, e.message ?: "Error de validación")
                )
            }
        }

        // Login de cliente
        post("/cliente/login") {
            try {
                val request = call.receive<ClienteLogin>()
                val authResponse = authService.loginCliente(request)
                if (authResponse != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(true, "Login exitoso", authResponse)
                    )
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Any>(false, "Credenciales inválidas")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Any>(false, "Error en el login: ${e.message}")
                )
            }
        }

        // Registro de administrador
        post("/admin/register") {
            try {
                val request = call.receive<AdministradorRequest>()
                val admin = authService.registerAdministrador(request)
                if (admin != null) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Administrador registrado exitosamente", admin)
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ApiResponse<Any>(false, "Error al registrar administrador")
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Any>(false, e.message ?: "Error de validación")
                )
            }
        }

        // Login de administrador
        post("/admin/login") {
            try {
                val request = call.receive<AdministradorLogin>()
                val authResponse = authService.loginAdministrador(request)
                if (authResponse != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(true, "Login exitoso", authResponse)
                    )
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Any>(false, "Credenciales inválidas")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Any>(false, "Error en el login: ${e.message}")
                )
            }
        }
    }
}