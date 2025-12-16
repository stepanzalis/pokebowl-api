package cz.pokebowl.routes

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*

fun Route.healthRoutes() {
    get("/health") {
        call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
    }
}