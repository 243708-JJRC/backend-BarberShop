package com.example.routes

import com.example.domain.services.AuthService
import com.example.domain.services.models.AdministradorLogin
import com.example.domain.services.models.AdministradorRequest
import com.example.domain.services.models.ApiResponse
import com.example.domain.services.models.ClienteLogin
import com.example.domain.services.models.ClienteRequest
import com.example.domain.services.models.LoginAdminResponse
import com.example.domain.services.models.LoginResponse
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.receive

fun Route.authRoutes(authService: AuthService) {
    route("/auth") {

                // Registro Cliente
                post("/cliente/register") {
                    try {
                        val request = call.receive<ClienteRequest>()
                        val cliente = authService.registerCliente(request)

                        if (cliente != null) {
                            call.respond(
                                HttpStatusCode.Created,
                                ApiResponse(
                                    success = true,
                                    message = "Cliente registrado exitosamente",
                                    data = cliente
                                )
                            )
                        } else {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                ApiResponse<Any>(
                                    success = false,
                                    message = "Error al registrar cliente"
                                )
                            )
                        }
                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ApiResponse<Any>(
                                success = false,
                                message = e.message ?: "Error de validación"
                            )
                        )
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            ApiResponse<Any>(
                                success = false,
                                message = "Error: ${e.message}"
                            )
                        )
                    }
                }

                // Login Cliente
                post("/cliente/login") {
                    try {
                        val request = call.receive<ClienteLogin>()
                        val cliente = authService.loginCliente(request)

                        if (cliente != null) {
                            call.respond(
                                HttpStatusCode.OK,
                                LoginResponse(
                                    success = true,
                                    message = "Login exitoso",
                                    cliente = cliente
                                )
                            )
                        } else {
                            call.respond(
                                HttpStatusCode.Unauthorized,
                                LoginResponse(
                                    success = false,
                                    message = "Email o contraseña incorrectos",
                                    cliente = null
                                )
                            )
                        }
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            LoginResponse(
                                success = false,
                                message = "Error en el login: ${e.message}",
                                cliente = null
                            )
                        )
                    }
                }

                // Registro Admin
                post("/admin/register") {
                    try {
                        val request = call.receive<AdministradorRequest>()
                        val admin = authService.registerAdministrador(request)

                        if (admin != null) {
                            call.respond(
                                HttpStatusCode.Created,
                                ApiResponse(
                                    success = true,
                                    message = "Administrador registrado exitosamente",
                                    data = admin
                                )
                            )
                        } else {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                ApiResponse<Any>(
                                    success = false,
                                    message = "Error al registrar administrador"
                                )
                            )
                        }
                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ApiResponse<Any>(
                                success = false,
                                message = e.message ?: "Error de validación"
                            )
                        )
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            ApiResponse<Any>(
                                success = false,
                                message = "Error: ${e.message}"
                            )
                        )
                    }
                }

                // Login Admin
                post("/admin/login") {
                    try {
                        val request = call.receive<AdministradorLogin>()
                        val admin = authService.loginAdministrador(request)

                        if (admin != null) {
                            call.respond(
                                HttpStatusCode.OK,
                                LoginAdminResponse(
                                    success = true,
                                    message = "Login exitoso",
                                    administrador = admin
                                )
                            )
                        } else {
                            call.respond(
                                HttpStatusCode.Unauthorized,
                                LoginAdminResponse(
                                    success = false,
                                    message = "Email o contraseña incorrectos",
                                    administrador = null
                                )
                            )
                        }
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            LoginAdminResponse(
                                success = false,
                                message = "Error en el login: ${e.message}",
                                administrador = null
                            )
                        )
                    }
                }
            }
        }