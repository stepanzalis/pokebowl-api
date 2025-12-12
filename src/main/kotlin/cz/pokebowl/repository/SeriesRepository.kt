package cz.pokebowl.repository

import cz.pokebowl.domain.Series
import cz.pokebowl.domain.SeriesTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

class SeriesRepository {
    fun upsert(seriesList: List<Series>) {
        transaction {
            seriesList.forEach { series ->
                SeriesTable.upsert {
                    it[id] = series.id
                    it[name] = series.name
                    it[logo] = series.logo
                }
            }
        }
    }

    fun findAll(): List<Series> = transaction {
        SeriesTable.selectAll().map { row ->
            Series(
                id = row[SeriesTable.id],
                name = row[SeriesTable.name],
                logo = row[SeriesTable.logo]
            )
        }
    }
}
