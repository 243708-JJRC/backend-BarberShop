package com.example

import com.example.config.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.cors.routing.*
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    install(CallLogging) {
        level = Level.INFO
    }

    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowMethod(io.ktor.http.HttpMethod.Options)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
        allowMethod(io.ktor.http.HttpMethod.Patch)
    }

    configureSerialization()
    configureStatusPages()
    configureRouting()
}