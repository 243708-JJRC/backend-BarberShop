package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object Negocios : Table("negocio") {
    val id = integer("id_negocio").autoIncrement()
    val nombreN = varchar("nombre_n", 100)
    val direccion = varchar("direccion", 250)

    override val primaryKey = PrimaryKey(id)
}