package cz.routes

import cz.services.FlywayService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.adminRoutes() {
    post("/migrate") {
        val migrationService by inject<FlywayService>()

        try {
            val clientSecret = call.request.headers["X-Secret-Key"]
            val serverSecret = System.getenv("SECRET_API_KEY")

            if (clientSecret != serverSecret) {
                call.respond(status = HttpStatusCode.Forbidden, "Access Forbidden")
                return@post
            }

            migrationService.runMigrations()

            call.respond(HttpStatusCode.OK, mapOf("success" to true))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf(
                "success" to false,
                "error" to e.message
            ))
        }
    }
}