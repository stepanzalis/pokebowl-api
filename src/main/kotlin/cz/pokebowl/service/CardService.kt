package cz.pokebowl.service

import cz.pokebowl.domain.Card
import cz.pokebowl.domain.dto.*
import cz.pokebowl.repository.CardRepository
import cz.pokebowl.repository.SeriesRepository
import cz.pokebowl.repository.SetRepository
import kotlin.math.ceil

class CardService(
    private val seriesRepository: SeriesRepository,
    private val setRepository: SetRepository,
    private val cardRepository: CardRepository
) {
    fun getSeriesWithSets(): List<SeriesWithSetsResponse> {
        val allSeries = seriesRepository.findAll()
        return allSeries.map { series ->
            val sets = setRepository.findBySeriesId(series.id)
            SeriesWithSetsResponse(
                id = series.id,
                name = series.name,
                logo = series.logo,
                sets = sets.map { set ->
                    SetResponse(
                        id = set.id,
                        name = set.name,
                        logo = set.logo,
                        symbol = set.symbol,
                        releaseDate = set.releaseDate,
                        data = set.data
                    )
                }
            )
        }
    }

    fun getCardsBySet(setId: String): List<CardResponse> {
        return cardRepository.findBySetId(setId).map { it.toResponse() }
    }

    fun getCardsPaginated(
        page: Int,
        pageSize: Int,
        sortBy: SortBy,
        sortOrder: SortOrder,
        minPrice: Double?,
        maxPrice: Double?
    ): PaginatedResponse<CardResponse> {
        val cards = cardRepository.findPaginated(page, pageSize, sortBy, sortOrder, minPrice, maxPrice)
        val totalItems = cardRepository.count(minPrice, maxPrice)
        val totalPages = ceil(totalItems.toDouble() / pageSize).toInt()

        return PaginatedResponse(
            items = cards.map { it.toResponse() },
            page = page,
            pageSize = pageSize,
            totalItems = totalItems,
            totalPages = totalPages
        )
    }

    private fun Card.toResponse() = CardResponse(
        id = id,
        setId = setId,
        image = image,
        localId = localId,
        name = name,
        rarity = rarity,
        avgPrice = avgPrice,
        data = data
    )
}
