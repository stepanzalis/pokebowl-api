package cz.pokebowl.di
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties("tcgdex", "fullSerie", "cards", "set")
interface TCGDexMixin
