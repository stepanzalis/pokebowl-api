package cz.pokebowl.repository

import cz.pokebowl.domain.SetsTable
import cz.pokebowl.domain.TCGSet
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SetRepository {
    fun upsert(setsList: List<TCGSet>) {
        transaction {
            setsList.forEach { set ->
                SetsTable.upsert {
                    it[id] = set.id
                    it[seriesId] = set.seriesId
                    it[name] = set.name
                    it[logo] = set.logo
                    it[symbol] = set.symbol
                    it[releaseDate] = set.releaseDate
                    it[data] = set.data
                }
            }
        }
    }

    fun findBySeriesId(seriesId: String): List<TCGSet> = transaction {
        SetsTable.selectAll().where { SetsTable.seriesId eq seriesId }.map { row ->
            TCGSet(
                id = row[SetsTable.id],
                seriesId = row[SetsTable.seriesId],
                name = row[SetsTable.name],
                logo = row[SetsTable.logo],
                symbol = row[SetsTable.symbol],
                releaseDate = row[SetsTable.releaseDate],
                data = row[SetsTable.data]
            )
        }
    }
}
