package com.example.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time

object Clientes : Table("cliente") {
    val id = integer("id_cliente").autoIncrement()
    val nombres = varchar("nombres", 100)
    val apellidoP = varchar("apellido_p", 50)
    val apellidoM = varchar("apellido_m", 50)
    val telefono = integer("telefono")
    val email = varchar("email", 100)
    val contraseña = varchar("contraseña", 100)
    val direccion = varchar("direccion", 250)

    override val primaryKey = PrimaryKey(id)
}

object Administradores : Table("administrador") {
    val id = integer("id_admin").autoIncrement()
    val nombres = varchar("nombres", 100)
    val apellidoP = varchar("apellido_p", 50)
    val apellidoM = varchar("apellido_m", 50)
    val telefono = integer("telefono")
    val email = varchar("email", 100)
    val contraseña = varchar("contraseña", 100)
    val negocioId = integer("negocio_id").references(Negocios.id).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Negocios : Table("negocio") {
    val id = integer("id_negocio").autoIncrement()
    val nombreN = varchar("nombre_n", 100)
    val direccion = varchar("direccion", 250)

    override val primaryKey = PrimaryKey(id)
}

object Horarios : Table("horario") {
    val id = integer("id_dia").autoIncrement()
    val dia = varchar("dia", 100)
    val horaApertura = time("hora_apertura").nullable()
    val horaCierre = time("hora_cierre").nullable()
    val negocioId = integer("negocio_id").references(Negocios.id)

    override val primaryKey = PrimaryKey(id)
}

object Servicios : Table("servicio") {
    val id = integer("id_servicio").autoIncrement()
    val nombre = varchar("nombre", 250)
    val precio = float("precio")
    val duracion = integer("duracion")
    val negocioId = integer("negocio_id").references(Negocios.id)

    override val primaryKey = PrimaryKey(id)
}

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