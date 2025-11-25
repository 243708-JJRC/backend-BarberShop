package com.example.routes

import com.example.config.getTokenClaims
import com.example.domain.services.NegocioService
import com.example.domain.services.models.ApiResponse
import com.example.domain.services.models.NegocioRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.negocioRoutes(negocioService: NegocioService) {
    route("/negocios") {

        get {
            try {
                val negocios = negocioService.getAllNegocios()
                call.respond(
                    ApiResponse(true, "Barberías obtenidas", negocios)
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Any>(false, "Error: ${e.message}")
                )
            }
        }

        // Obtener barbería completa (con horarios y servicios)
        get("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val negocio = negocioService.getNegocioCompleto(id)
                if (negocio != null) {
                    call.respond(ApiResponse(true, "Barbería encontrada", negocio))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Any>(false, "Barbería no encontrada")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Any>(false, "Error: ${e.message}")
                )
            }
        }

        authenticate("auth-jwt") {
            // Crear barbería (solo admin)
            post {
                try {
                    val principal = call.principal<JWTPrincipal>()!!
                    val claims = principal.getTokenClaims()

                    if (claims.role != "administrador") {
                        return@post call.respond(
                            HttpStatusCode.Forbidden,
                            ApiResponse<Any>(false, "No autorizado")
                        )
                    }

                    val request = call.receive<NegocioRequest>()
                    val negocio = negocioService.createNegocio(request)
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Barbería creada", negocio)
                    )
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Any>(false, e.message ?: "Error")
                    )
                }
            }

            // Actualizar barbería
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val request = call.receive<NegocioRequest>()
                val updated = negocioService.updateNegocio(id, request)
                call.respond(
                    if (updated) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(updated, if (updated) "Actualizada" else "No encontrada", null)
                )
            }

            // Eliminar barbería
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, ApiResponse<Any>(false, "ID inválido")
                )
                val deleted = negocioService.deleteNegocio(id)
                call.respond(
                    if (deleted) HttpStatusCode.OK else HttpStatusCode.NotFound,
                    ApiResponse(deleted, if (deleted) "Eliminada" else "No encontrada", null)
                )
            }
        }
    }
}