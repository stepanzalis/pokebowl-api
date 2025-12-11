package cz.pokebowl.repository

import cz.pokebowl.domain.Series
import cz.pokebowl.domain.SeriesTable
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
}
