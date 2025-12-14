package cz.pokebowl.routes

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

fun Route.healthRoutes() {

    val database by inject<Database>()

    get("/health") {
        try {
            transaction(database) {
                exec("SELECT 1") { /* Ignored */ }
            }

            call.respond(HttpStatusCode.OK, mapOf("status" to "ok", "db" to "connected"))

        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("status" to "error", "details" to e.localizedMessage)
            )
        }
    }
}