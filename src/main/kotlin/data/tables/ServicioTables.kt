package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object Servicios : Table("servicio") {
    val id = integer("id_servicio").autoIncrement()
    val nombre = varchar("nombre", 250)
    val precio = float("precio")
    val duracion = integer("duracion")
    val negocioId = integer("negocio_id").references(Negocios.id)

    override val primaryKey = PrimaryKey(id)
}