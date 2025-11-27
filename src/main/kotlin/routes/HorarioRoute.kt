package com.example.routes

import com.example.domain.services.NegocioService
import com.example.domain.services.models.ApiResponse
import com.example.domain.services.models.HorarioRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.horarioRoutes(negocioService: NegocioService) {
        route("/horarios") {

            post {
                try {
                    val request = call.receive<HorarioRequest>()
                    val horario = negocioService.addHorario(request)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Horario agregado", horario)
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
                val request = call.receive<HorarioRequest>()
                val updated = negocioService.updateHorario(id, request)
                call.respond(
                    if (updated) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(updated, if (updated) "Actualizado" else "No encontrado", null)
                )
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val deleted = negocioService.deleteHorario(id)
                call.respond(
                    if (deleted) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(deleted, if (deleted) "Eliminado" else "No encontrado", null)
                )
            }
        }
    }