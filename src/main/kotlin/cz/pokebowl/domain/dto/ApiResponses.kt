package cz.pokebowl.domain.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// --- Series & Sets ---

@Serializable
data class SeriesWithSetsResponse(
    val id: String,
    val name: String,
    val logo: String?,
    val sets: List<SetResponse>
)

@Serializable
data class SetResponse(
    val id: String,
    val name: String,
    val logo: String?,
    val symbol: String?,
    val releaseDate: String?,
    val data: JsonObject
)

// --- Cards ---

@Serializable
data class CardResponse(
    val id: String,
    val setId: String,
    val image: String?,
    val localId: String?,
    val name: String,
    val rarity: String?,
    val avgPrice: Double?,
    val data: JsonObject
)

@Serializable
data class CardsByIdsRequest(
    val ids: List<String>
)

// --- Pagination ---

@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
)
