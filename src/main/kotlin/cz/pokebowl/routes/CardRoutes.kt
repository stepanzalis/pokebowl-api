package cz.pokebowl.routes

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
        val sortBy = call.request.queryParameters["sortBy"]?.let { 
            try { cz.pokebowl.domain.dto.SortBy.valueOf(it.uppercase()) } catch (e: Exception) { null }
        } ?: cz.pokebowl.domain.dto.SortBy.NAME
        val sortOrder = call.request.queryParameters["sortOrder"]?.let {
            try { cz.pokebowl.domain.dto.SortOrder.valueOf(it.uppercase()) } catch (e: Exception) { null }
        } ?: cz.pokebowl.domain.dto.SortOrder.ASCENDING
        val minPrice = call.request.queryParameters["minPrice"]?.toDoubleOrNull()
        val maxPrice = call.request.queryParameters["maxPrice"]?.toDoubleOrNull()

        val result = cardService.getCardsPaginated(page, pageSize, sortBy, sortOrder, minPrice, maxPrice)
        call.respond(result)
    }
}
