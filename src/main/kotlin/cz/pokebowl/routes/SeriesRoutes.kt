package cz.pokebowl.routes

import cz.pokebowl.service.CardService
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.seriesRoutes() {
    val cardService by inject<CardService>()

    get("/series") {
        val seriesWithSets = cardService.getSeriesWithSets()
        call.respond(seriesWithSets)
    }
}
