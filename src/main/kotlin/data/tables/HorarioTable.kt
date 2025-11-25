package com.example.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time

object Horarios : Table("horario") {
    val id = integer("id_dia").autoIncrement()
    val dia = varchar("dia", 100)
    val horaApertura = time("hora_apertura").nullable()
    val horaCierre = time("hora_cierre").nullable()
    val negocioId = integer("negocio_id").references(Negocios.id)

    override val primaryKey = PrimaryKey(id)
}