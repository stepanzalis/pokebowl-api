package cz.pokebowl.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.pokebowl.domain.Card
import cz.pokebowl.domain.dto.TCGDexCardResponse
import cz.pokebowl.domain.Series
import cz.pokebowl.domain.TCGSet
import cz.pokebowl.repository.CardRepository
import cz.pokebowl.repository.SeriesRepository
import cz.pokebowl.repository.SetRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.*

import kotlinx.coroutines.*
import net.tcgdex.sdk.TCGdex

class TCGDexService(
    private val tcgDex: TCGdex,
    private val seriesRepository: SeriesRepository,
    private val setRepository: SetRepository,
    private val cardRepository: CardRepository,
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient,
) {

    fun syncSeries() {
        tcgDex.fetchSeries()?.let {
            val mappedSeries = it.map { series ->
                Series(
                    id = series.id,
                    name = series.name,
                    logo = series.logo
                )
            }

            seriesRepository.upsert(mappedSeries)
        }
    }

    fun syncSets() {
        tcgDex.fetchSets()?.let {
            val ignoredKeys = setOf("id", "name", "serie", "logo", "symbol", "releaseDate")

            val domainSets = it.mapNotNull { setSummary ->
                val detail = tcgDex.fetchSet(setSummary.id)
                detail?.let { set ->
                    val extraDataMap = objectMapper.convertValue<Map<String, Any?>>(set) - ignoredKeys
                    val jsonElement = Json.parseToJsonElement(objectMapper.writeValueAsString(extraDataMap)).jsonObject

                    TCGSet(
                        id = set.id ?: "",
                        name = set.name ?: "",
                        seriesId = set.serie.id,
                        logo = set.logo,
                        symbol = set.symbol,
                        releaseDate = set.releaseDate,
                        data = jsonElement
                    )
                }
            }

            setRepository.upsert(domainSets)
        }
    }

    suspend fun syncCards() = coroutineScope {
        val sets = withContext(Dispatchers.IO) { tcgDex.fetchSets() }

        sets?.forEach { setBrief ->
            val setDetails = withContext(Dispatchers.IO) { tcgDex.fetchSet(setBrief.id) }

            setDetails?.cards?.chunked(50)?.forEach { batch ->
                val domainCards = batch.map { cardResume ->
                    async(Dispatchers.IO) {
                        fetchAndMapCard(cardResume.id, setDetails.id ?: "")
                    }
                }.awaitAll().filterNotNull()

                if (domainCards.isNotEmpty()) {
                    cardRepository.upsert(domainCards)
                }
            }
        }
    }

    private suspend fun fetchAndMapCard(cardId: String, setId: String): Card? {
        return try {
            val response = httpClient.get("https://api.tcgdex.net/v2/en/cards/$cardId").body<TCGDexCardResponse>()

            // Extract pricing
            val avgPrice = response.pricing?.cardmarket?.avg
                ?: response.pricing?.tcgplayer?.normal?.marketPrice
                ?: 0.0

            // Filter data to store in JSONB
            // Convert DTO to Map to remove ignored keys
            val fullMap = objectMapper.convertValue<Map<String, Any?>>(response)
            val extraDataMap = fullMap - IGNORED_CARD_KEYS
            val extraData = Json.parseToJsonElement(objectMapper.writeValueAsString(extraDataMap)).jsonObject

            Card(
                id = response.id,
                setId = setId,
                image = response.image,
                localId = response.localId,
                name = response.name,
                rarity = response.rarity,
                avgPrice = avgPrice,
                data = extraData
            )
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private val IGNORED_CARD_KEYS = setOf("id", "image", "localId", "name", "rarity", "set", "serie")
    }
}
