package com.example.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {

    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.mariadb.jdbc.Driver"
            jdbcUrl = System.getenv("DB_URL") ?: "jdbc:mariadb://localhost:3306/barber_shop2"
            username = System.getenv("DB_USER") ?: "root"
            password = System.getenv("DB_PASSWORD") ?: ""
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        Database.connect(HikariDataSource(config))
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}