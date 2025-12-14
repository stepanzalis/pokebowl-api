package cz.pokebowl.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import cz.pokebowl.config.AppConfig
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases(config: AppConfig) {
    val dbConfig = config.database
    val hikariConfig = HikariConfig().apply {
        driverClassName = dbConfig.driverClassName
        jdbcUrl = dbConfig.jdbcUrl
        username = dbConfig.username
        password = dbConfig.password
        maximumPoolSize = dbConfig.maximumPoolSize
        isAutoCommit = dbConfig.isAutoCommit
        transactionIsolation = dbConfig.transactionIsolation
        minimumIdle = dbConfig.minimumIdleConnection
        validate()
    }

    val dataSource = HikariDataSource(hikariConfig)

    // Run Flyway migrations
    val flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migrations")
        .baselineOnMigrate(true)
        .load()
    
    try {
        flyway.migrate()
    } catch (e: Exception) {
        log.error("Flyway migration failed", e)
        // Decide if we want to crash or continue. Crashing is usually safer for DB/Schema mismatches.
        throw e
    }

    Database.connect(dataSource)
    log.info("Database connected")
}

