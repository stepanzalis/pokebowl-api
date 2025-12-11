package cz.pokebowl.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json
// import org.jetbrains.exposed.sql.javatime.date // Removed to avoid potential dependency conflict if not configured
import java.time.LocalDate

@Serializable

data class TCGSet(
    val id: String,
    val seriesId: String,
    val name: String,
    val logo: String?,
    val symbol: String?,
    val releaseDate: String?,
    val data: JsonObject = JsonObject(emptyMap())
)

object SetsTable : Table("sets") {
    val id = varchar("id", 255)
    val seriesId = varchar("series_id", 255).references(SeriesTable.id)
    val name = varchar("name", 255)
    val logo = varchar("logo", 500).nullable()
    val symbol = varchar("symbol", 500).nullable()
    val releaseDate = varchar("release_date", 50).nullable()
    // Using simple generic map for JSONB for now, can be typed later
    val data = json<JsonObject>("data", Json)

    override val primaryKey = PrimaryKey(id)
}
