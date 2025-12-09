package cz.services

import org.jetbrains.exposed.sql.*

class DatabaseService {

    private lateinit var database: Database

    private val dbDriver = "org.postgresql.Driver"

    fun connect() {
        database = Database.connect(
            url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5433/tcg_db?sslmode=disable",
            driver = dbDriver,
            user = System.getenv("DB_USER") ?: "stepanzalis",
            password = System.getenv("DB_PASS") ?: "stepanzalis"
        )
    }
}