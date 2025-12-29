package cz.pokebowl.repository

import cz.pokebowl.domain.Card
import cz.pokebowl.domain.CardsTable
import cz.pokebowl.domain.dto.SortBy
import cz.pokebowl.domain.dto.SortOrder
import org.jetbrains.exposed.sql.LowerCase
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

class CardRepository {
    fun upsert(cardsList: List<Card>) {
        transaction {
            cardsList.forEach { card ->
                CardsTable.upsert {
                    it[id] = card.id
                    it[setId] = card.setId
                    it[image] = card.image
                    it[localId] = card.localId
                    it[name] = card.name
                    it[rarity] = card.rarity
                    it[avgPrice] = card.avgPrice?.toBigDecimal()
                    it[data] = card.data
                }
            }
        }
    }

    fun findBySetId(setId: String): List<Card> = transaction {
        CardsTable.selectAll()
            .where { CardsTable.setId eq setId }
            .sortedBy { CardsTable.localId }.map { row ->
                rowToCard(row)
        }
    }

    fun findByIds(ids: List<String>): List<Card> = transaction {
        if (ids.isEmpty()) return@transaction emptyList()
        CardsTable.selectAll().where { CardsTable.id inList ids }.map { row ->
            rowToCard(row)
        }
    }

    fun findByName(name: String): List<Card> = transaction {
        CardsTable.selectAll()
            .where { LowerCase(CardsTable.name) eq name.lowercase() }
            .map { row -> rowToCard(row) }
    }

    fun search(query: String, limit: Int = 50): List<Card> = transaction {
        val lowerQuery = "%${query.lowercase()}%"
        CardsTable.selectAll()
            .where { 
                (LowerCase(CardsTable.name) like lowerQuery) or 
                (LowerCase(CardsTable.localId) like lowerQuery) 
            }
            .limit(limit)
            .map { row -> rowToCard(row) }
    }

    fun findPaginated(
        page: Int,
        pageSize: Int,
        sortBy: SortBy,
        sortOrder: SortOrder,
        minPrice: Double?,
        maxPrice: Double?
    ): List<Card> = transaction {
        val sortColumn = when (sortBy) {
            SortBy.AVG_PRICE -> CardsTable.avgPrice
            SortBy.NAME -> CardsTable.name
        }

        val order = when (sortOrder) {
            SortOrder.DESCENDING -> org.jetbrains.exposed.sql.SortOrder.DESC
            SortOrder.ASCENDING -> org.jetbrains.exposed.sql.SortOrder.ASC
        }

        var query = CardsTable.selectAll()
        
        minPrice?.let {
            query = query.andWhere { CardsTable.avgPrice greaterEq minPrice.toBigDecimal() }
        }

        maxPrice?.let {
            query = query.andWhere { CardsTable.avgPrice lessEq maxPrice.toBigDecimal() }
        }

        query
            .orderBy(sortColumn to order)
            .limit(pageSize)
            .offset(((page - 1) * pageSize).toLong())
            .map { row -> rowToCard(row) }
    }

    fun count(minPrice: Double?, maxPrice: Double?): Long = transaction {
        var query = CardsTable.selectAll()

        minPrice?.let {
            query = query.andWhere { CardsTable.avgPrice greaterEq minPrice.toBigDecimal() }
        }
        maxPrice?.let {
            query = query.andWhere { CardsTable.avgPrice lessEq maxPrice.toBigDecimal() }
        }

        query.count()
    }

    private fun rowToCard(row: org.jetbrains.exposed.sql.ResultRow): Card = Card(
        id = row[CardsTable.id],
        setId = row[CardsTable.setId],
        image = row[CardsTable.image],
        localId = row[CardsTable.localId],
        name = row[CardsTable.name],
        rarity = row[CardsTable.rarity],
        avgPrice = row[CardsTable.avgPrice]?.toDouble(),
        data = row[CardsTable.data]
    )
}
