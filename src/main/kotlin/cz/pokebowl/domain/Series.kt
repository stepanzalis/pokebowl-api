package cz.pokebowl.domain

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Series(
    val id: String,
    val name: String,
    val logo: String?
)

object SeriesTable : Table("series") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val logo = varchar("logo", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}
