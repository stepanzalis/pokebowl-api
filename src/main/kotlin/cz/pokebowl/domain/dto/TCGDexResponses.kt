package cz.pokebowl.domain.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class TCGDexCardResponse(
    val id: String,
    val localId: String? = null,
    val name: String,
    val image: String? = null,
    val illustrator: String? = null,
    val rarity: String? = null,
    val category: String? = null,
    val variants: TCGDexVariants? = null,
    val set: TCGDexSetResume,
    val pricing: TCGDexPricing? = null,
    val dexId: List<Int>? = null,
    val hp: Int? = null,
    val types: List<String>? = null,
    val evolveFrom: String? = null,
    val description: String? = null,
    val stage: String? = null,
    val attacks: List<TCGDexAttack>? = null,
    val weaknesses: List<TCGDexWeakRes>? = null,
    val retreat: Int? = null,
    val regulationMark: String? = null,
    val legal: TCGDexLegal? = null,
    val updated: String? = null
)

@Serializable
data class TCGDexVariants(
    val normal: Boolean? = null,
    val reverse: Boolean? = null,
    val holo: Boolean? = null,
    val firstEdition: Boolean? = null,
    val wPromo: Boolean? = null
)

@Serializable
data class TCGDexSetResume(
    val id: String,
    val name: String,
    val logo: String? = null,
    val symbol: String? = null,
    val cardCount: TCGDexCardCount? = null
)

@Serializable
data class TCGDexCardCount(
    val official: Int,
    val total: Int
)

@Serializable
data class TCGDexPricing(
    val tcgplayer: TCGDexPricingTcgPlayer? = null,
    val cardmarket: TCGDexPricingCardMarket? = null
)

@Serializable
data class TCGDexPricingCardMarket(
    val updated: String? = null,
    val unit: String? = null,
    val avg: Double? = null,
    val low: Double? = null,
    val trend: Double? = null,
    val avg1: Double? = null,
    val avg7: Double? = null,
    val avg30: Double? = null,
    @SerialName("avg-holo") val avgHolo: Double? = null,
    @SerialName("low-holo") val lowHolo: Double? = null,
    @SerialName("trend-holo") val trendHolo: Double? = null,
    @SerialName("avg1-holo") val avg1Holo: Double? = null,
    @SerialName("avg7-holo") val avg7Holo: Double? = null,
    @SerialName("avg30-holo") val avg30Holo: Double? = null
)

@Serializable
data class TCGDexPricingTcgPlayer(
    val updated: String? = null,
    val unit: String? = null,
    val normal: TCGDexPricingVariant? = null,
    @SerialName("reverse-holofoil") val reverseHolofoil: TCGDexPricingVariant? = null,
    @SerialName("1st-edition-holofoil") val firstEditionHolofoil: TCGDexPricingVariant? = null,
    @SerialName("1st-edition") val firstEdition: TCGDexPricingVariant? = null
)

@Serializable
data class TCGDexPricingVariant(
    val lowPrice: Double? = null,
    val midPrice: Double? = null,
    val highPrice: Double? = null,
    val marketPrice: Double? = null,
    val directLowPrice: Double? = null
)

@Serializable
data class TCGDexAttack(
    val name: String,
    val cost: List<String>? = null,
    val effect: String? = null,
    val damage: JsonElement? = null 
)

@Serializable
data class TCGDexWeakRes(
    val type: String,
    val value: String? = null
)

@Serializable
data class TCGDexLegal(
    val standard: Boolean? = null,
    val expanded: Boolean? = null
)
