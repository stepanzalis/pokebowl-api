package cz.routes

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.registerRoutes() {
    routing {
        route("/api/v1") {
            healthRoutes()

            route("/admin") {
                adminRoutes()
            }
        }
    }
}