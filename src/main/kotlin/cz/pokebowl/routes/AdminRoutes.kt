package cz.pokebowl.routes

import cz.pokebowl.config.AppConfig
import cz.pokebowl.service.TCGDexService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Route.adminRoutes() {
    val tcgDexService by inject<TCGDexService>()
    val appConfig by inject<AppConfig>()

    route("/admin/sync") {
        install(createRouteScopedPlugin("AdminAuthPlugin") {
            onCall { call ->
                val secret = call.request.headers["X-Admin-Secret"]
                if (secret != appConfig.admin.secret) {
                    call.respond(HttpStatusCode.Forbidden, "Invalid admin secret")
                }
            }
        })

        post("/series") {
            try {
                tcgDexService.syncSeries()
                call.respond(HttpStatusCode.OK, mapOf("status" to "Series synced successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }

        post("/sets") {
            try {
                tcgDexService.syncSets()
                call.respond(HttpStatusCode.OK, mapOf("status" to "Sets synced successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }

        post("/cards") {
            try {
                tcgDexService.syncCards()
                call.respond(HttpStatusCode.OK, mapOf("status" to "Cards synced successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }
    }
}
