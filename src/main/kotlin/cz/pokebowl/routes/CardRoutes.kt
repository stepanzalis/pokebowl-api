package cz.pokebowl.routes

import cz.pokebowl.domain.dto.SortBy
import cz.pokebowl.domain.dto.SortOrder
import cz.pokebowl.service.CardService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.cardRoutes() {
    val cardService by inject<CardService>()

    // Cards by set
    get("/sets/{setId}/cards") {
        val setId = call.parameters["setId"] ?: return@get call.respond(
            HttpStatusCode.BadRequest,
            mapOf("error" to "setId is required")
        )
        val cards = cardService.getCardsBySet(setId)
        call.respond(cards)
    }

    // Paginated cards with filtering and sorting
    get("/cards") {
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
        val sortBy = SortBy.fromStringOrNull(call.request.queryParameters["sortBy"]) ?: SortBy.NAME
        val sortOrder = SortOrder.fromStringOrNull(call.request.queryParameters["sortOrder"]) ?: SortOrder.ASCENDING
        val minPrice = call.request.queryParameters["minPrice"]?.toDoubleOrNull()
        val maxPrice = call.request.queryParameters["maxPrice"]?.toDoubleOrNull()

        val result = cardService.getCardsPaginated(page, pageSize, sortBy, sortOrder, minPrice, maxPrice)
        call.respond(result)
    }
}
