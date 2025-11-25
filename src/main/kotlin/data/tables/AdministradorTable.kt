package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object Administradores : Table("administrador") {
    val id = integer("id_admin").autoIncrement()
    val nombres = varchar("nombres", 100)
    val apellidoP = varchar("apellido_p", 50)
    val apellidoM = varchar("apellido_m", 50)
    val telefono = varchar("telefono",20)
    val email = varchar("email", 100)
    val contraseña = varchar("contraseña", 100)
    val negocioId = integer("negocio_id").references(Negocios.id).nullable()

    override val primaryKey = PrimaryKey(id)
}