package com.example.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Citas : Table("citas") {
    val id = integer("id_citas").autoIncrement()
    val fechaRealizacion = date("fecha_realizacion").nullable()
    val fechaCita = date("fecha_cita")
    val precio = float("precio")
    val asunto = varchar("asunto", 200)
    val estado = varchar("estado", 50).default("pendiente")
    val clienteId = integer("cliente_id").references(Clientes.id)
    val negocioId = integer("negocio_id").references(Negocios.id)
    val servicioId = integer("servicio_id").references(Servicios.id)

    override val primaryKey = PrimaryKey(id)
}