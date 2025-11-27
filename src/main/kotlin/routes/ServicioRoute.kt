package com.example.routes

import com.example.domain.services.NegocioService
import com.example.domain.services.models.ServicioRequest
import com.example.domain.services.models.ApiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.servicioRoutes(negocioService: NegocioService) {
        route("/servicios") {

            post {
                try {
                    val request = call.receive<ServicioRequest>()
                    val servicio = negocioService.addServicio(request)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Servicio agregado", servicio)
                    )
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Any>(false, e.message ?: "Error")
                    )
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val request = call.receive<ServicioRequest>()
                val updated = negocioService.updateServicio(id, request)
                call.respond(
                    if (updated) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(updated, if (updated) "Actualizado" else "No encontrado", null)
                )
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val deleted = negocioService.deleteServicio(id)
                call.respond(
                    if (deleted) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(deleted, if (deleted) "Eliminado" else "No encontrado", null)
                )
            }
        }
    }
