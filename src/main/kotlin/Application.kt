package cz

import appModule
import cz.ext.RateLimitConfig.Companion.GLOBAL_MAX_REQUESTS
import cz.ext.RateLimitConfig.Companion.PROTECTED_MAX_REQUESTS
import cz.ext.RateLimitConfig.Companion.PROTECTED_RATE_LIMIT
import cz.ext.RateLimitConfig.Companion.PROTECTED_REFILL_PERIOD
import cz.routes.registerRoutes
import cz.services.DatabaseService
import cz.services.FlywayService
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level
import kotlin.getValue

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toIntOrNull() ?: 5556,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(CallLogging) { level =  Level.INFO }

    install(RateLimit) {
        register(RateLimitName(PROTECTED_RATE_LIMIT)) {
            rateLimiter(
                limit = PROTECTED_MAX_REQUESTS,
                refillPeriod = PROTECTED_REFILL_PERIOD
            )
        }
        global {
            rateLimiter(
                limit = GLOBAL_MAX_REQUESTS,
                refillPeriod = PROTECTED_REFILL_PERIOD
            )
        }
    }

    install(Koin) {
        modules(appModule)
    }

    install(ContentNegotiation) {
        json(
            contentType = Json,
            json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "Internal Server Error" , status = HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }
    }

    val migration by inject<FlywayService>()
    migration.runMigrations()

    val database by inject<DatabaseService>()

    database.connect()
    registerRoutes()
}
