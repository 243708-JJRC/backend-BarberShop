package com.example.routes

import com.example.domain.services.CitaService
import com.example.domain.services.models.ActualizarEstadoCita
import com.example.domain.services.models.ApiResponse
import com.example.domain.services.models.CitaRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond

fun Route.citaRoutes(citaService: CitaService) {
    authenticate("auth-jwt") {
        route("/citas") {

            // Historial de citas del cliente
            get("/cliente/{clienteId}") {
                try {
                    val clienteId = call.parameters["clienteId"]?.toIntOrNull() ?: return@get call.respond(
                        HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                    )
                    val citas = citaService.getCitasByCliente(clienteId)
                    call.respond(ApiResponse(true, "Historial de citas", citas))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ApiResponse<Any>(false, "Error: ${e.message}")
                    )
                }
            }

            // Citas pendientes del cliente
            get("/cliente/{clienteId}/pendientes") {
                try {
                    val clienteId = call.parameters["clienteId"]?.toIntOrNull() ?: return@get call.respond(
                        HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                    )
                    val citas = citaService.getCitasPendientesByCliente(clienteId)
                    call.respond(ApiResponse(true, "Citas pendientes", citas))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ApiResponse<Any>(false, "Error: ${e.message}")
                    )
                }
            }

            // Historial de clientes de la barbería
            get("/negocio/{negocioId}") {
                try {
                    val negocioId = call.parameters["negocioId"]?.toIntOrNull() ?: return@get call.respond(
                        HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                    )
                    val citas = citaService.getCitasByNegocio(negocioId)
                    call.respond(ApiResponse(true, "Historial de clientes", citas))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ApiResponse<Any>(false, "Error: ${e.message}")
                    )
                }
            }

            // Reservar cita
            post {
                try {
                    val request = call.receive<CitaRequest>()
                    val cita = citaService.createCita(request)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Cita reservada exitosamente", cita)
                    )
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Any>(false, e.message ?: "Error")
                    )
                }
            }

            // Actualizar estado de cita
            patch("/{id}/estado") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(
                        HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                    )
                    val request = call.receive<ActualizarEstadoCita>()
                    val updated = citaService.updateEstadoCita(id, request.estado)
                    call.respond(
                        if (updated) HttpStatusCode.OK else HttpStatusCode.NotFound,
                        ApiResponse(updated, if (updated) "Estado actualizado" else "Cita no encontrada", null)
                    )
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Any>(false, e.message ?: "Error")
                    )
                }
            }

            // Cancelar cita
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val cancelled = citaService.cancelarCita(id)
                call.respond(
                    if (cancelled) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(cancelled, if (cancelled) "Cita cancelada" else "Cita no encontrada", null)
                )
            }
        }
    }
}