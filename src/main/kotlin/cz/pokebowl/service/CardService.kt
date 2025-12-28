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
    private val cardRepository: CardRepository,
    private val cacheService: CacheService
) {
    companion object {
        private const val CACHE_KEY_SERIES_WITH_SETS = "series_with_sets"
        private const val CACHE_KEY_CARDS_PREFIX = "cards_paginated"
    }

    fun getSeriesWithSets(): List<SeriesWithSetsResponse> {
        // Try cache first
        cacheService.get<List<SeriesWithSetsResponse>>(CACHE_KEY_SERIES_WITH_SETS)?.let { return it }

        val allSeries = seriesRepository.findAll()
        val result = allSeries.map { series ->
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

        cacheService.set(CACHE_KEY_SERIES_WITH_SETS, result)
        return result
    }

    fun getCardsBySet(setId: String): List<CardResponse> {
        return cardRepository.findBySetId(setId).map { it.toResponse() }
    }

    fun getCardsByIds(ids: List<String>): List<CardResponse> {
        return cardRepository.findByIds(ids).map { it.toResponse() }
    }

    fun getCardsPaginated(
        page: Int,
        pageSize: Int,
        sortBy: SortBy,
        sortOrder: SortOrder,
        minPrice: Double?,
        maxPrice: Double?
    ): PaginatedResponse<CardResponse> {
        val cacheKey = "$CACHE_KEY_CARDS_PREFIX:$page:$pageSize:$sortBy:$sortOrder:$minPrice:$maxPrice"

        // Try cache first
        cacheService.get<PaginatedResponse<CardResponse>>(cacheKey)?.let { return it }

        val cards = cardRepository.findPaginated(page, pageSize, sortBy, sortOrder, minPrice, maxPrice)
        val totalItems = cardRepository.count(minPrice, maxPrice)
        val totalPages = ceil(totalItems.toDouble() / pageSize).toInt()

        val result = PaginatedResponse(
            items = cards.map { it.toResponse() },
            page = page,
            pageSize = pageSize,
            totalItems = totalItems,
            totalPages = totalPages
        )

        cacheService.set(cacheKey, result)
        return result
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
