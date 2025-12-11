package cz.pokebowl.repository

import cz.pokebowl.domain.Card
import cz.pokebowl.domain.CardsTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.math.BigDecimal

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
}
