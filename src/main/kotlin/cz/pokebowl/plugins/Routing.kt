package cz.pokebowl.plugins

import cz.pokebowl.routes.adminRoutes
import cz.pokebowl.routes.cardRoutes
import cz.pokebowl.routes.healthRoutes
import cz.pokebowl.routes.seriesRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            // Admin
            adminRoutes()

            // Health
            healthRoutes()

            // Public API
            seriesRoutes()
            cardRoutes()
        }
    }
}

