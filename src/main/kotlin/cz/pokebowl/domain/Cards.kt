package cz.pokebowl.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json
import java.math.BigDecimal

@Serializable
data class Card(
    val id: String,
    val setId: String,
    val image: String?,
    val localId: String?,
    val name: String,
    val rarity: String?,
    val avgPrice: Double?,
    val data: JsonObject = JsonObject(emptyMap())
)

object CardsTable : Table("cards") {
    val id = varchar("id", 255)
    val setId = varchar("set_id", 255).references(SetsTable.id)
    val image = varchar("image", 500).nullable()
    val localId = varchar("local_id", 100).nullable()
    val name = varchar("name", 255)
    val rarity = varchar("rarity", 100).nullable()
    val avgPrice = decimal("avg_price", 10, 2).nullable()
    val data = json<JsonObject>("data", Json)

    override val primaryKey = PrimaryKey(id)
}