package com.example.config

import com.example.domain.services.models.ApiResponse
import io.ktor.server.application.Application
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.log.error("Error no manejado", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse<Any>(
                    success = false,
                    message = "Error interno del servidor: ${cause.message}"
                )
            )
        }

        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Any>(
                    success = false,
                    message = cause.message ?: "Solicitud inválida"
                )
            )
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ApiResponse<Any>(
                    success = false,
                    message = "Recurso no encontrado"
                )
            )
        }

        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ApiResponse<Any>(
                    success = false,
                    message = "No autorizado - Token inválido o expirado"
                )
            )
        }
    }
}