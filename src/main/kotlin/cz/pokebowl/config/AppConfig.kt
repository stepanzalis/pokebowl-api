package cz.pokebowl.config

import io.ktor.server.config.*

class AppConfig(config: ApplicationConfig) {
    val port: Int = config.property("ktor.deployment.port").getString().toInt()
    
    val database = DatabaseConfig(config)
    
    class DatabaseConfig(config: ApplicationConfig) {
        val driverClassName = config.propertyOrNull("storage.driverClassName")?.getString() ?: "org.postgresql.Driver"
        val jdbcUrl = config.propertyOrNull("storage.jdbcUrl")?.getString() ?: "jdbc:postgresql://localhost:5433/tcg_db"
        val username = config.propertyOrNull("storage.username")?.getString() ?: "stepanzalis"
        val password = config.propertyOrNull("storage.password")?.getString() ?: "stepanzalis"
        val maximumPoolSize = config.propertyOrNull("storage.maximumPoolSize")?.getString()?.toInt() ?: 10
        val isAutoCommit = config.propertyOrNull("storage.isAutoCommit")?.getString()?.toBoolean() ?: false
        val transactionIsolation = config.propertyOrNull("storage.transactionIsolation")?.getString() ?: "TRANSACTION_REPEATABLE_READ"
    }

    val redis = RedisConfig(config)

    class RedisConfig(config: ApplicationConfig) {
        val host = config.propertyOrNull("redis.host")?.getString() ?: "localhost"
        val port = config.propertyOrNull("redis.port")?.getString()?.toInt() ?: 6379
        val ttlSeconds = config.propertyOrNull("redis.ttlSeconds")?.getString()?.toLong() ?: 300L
    }

    val admin = AdminConfig(config)

    class AdminConfig(config: ApplicationConfig) {
        val secret = config.propertyOrNull("admin.secret")?.getString() ?: "admin-secret"
    }
}

