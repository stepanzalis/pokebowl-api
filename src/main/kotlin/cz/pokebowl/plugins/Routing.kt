package cz.pokebowl.plugins

import cz.pokebowl.routes.adminRoutes
import cz.pokebowl.routes.healthRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            // Admin
            adminRoutes()

            // Health
            healthRoutes()
        }
    }
}

