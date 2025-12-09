package cz.services

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import org.koin.core.component.KoinComponent

class FlywayService {

    fun runMigrations(): MigrateResult {
        val url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5433/tcg_db?sslmode=disable"
        val user = System.getenv("DB_USER") ?: "stepanzalis"
        val password = System.getenv("DB_PASS") ?: "stepanzalis"

        val flyway = Flyway.configure()
            .dataSource(url, user, password)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load()

        return flyway.migrate()
    }
}