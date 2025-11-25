package com.example.config

import com.example.data.tables.repositories.*
import com.example.domain.services.*
import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    val clienteRepository = ClienteRepositoryImpl()
    val administradorRepository = AdministradorRepositoryImpl()
    val horarioRepository = HorarioRepositoryImpl()
    val servicioRepository = ServicioRepositoryImpl()
    val negocioRepository = NegocioRepositoryImpl(horarioRepository, servicioRepository)
    val citaRepository = CitaRepositoryImpl(clienteRepository, negocioRepository, servicioRepository)

    val authService = AuthService(clienteRepository, administradorRepository)
    val negocioService = NegocioService(negocioRepository, horarioRepository, servicioRepository)
    val citaService = CitaService(citaRepository, servicioRepository)

    routing {
        route("/api") {
            authRoutes(authService)
            negocioRoutes(negocioService)

            horarioRoutes(negocioService)
            servicioRoutes(negocioService)
            citaRoutes(citaService)

            get("/health") {
                call.respond(
                    mapOf(
                        "status" to "OK",
                        "message" to "Barbershop API funcionando correctamente",
                        "version" to "1.0.0"
                    )
                )
            }
        }
    }
}