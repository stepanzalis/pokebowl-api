package cz.pokebowl.di

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import cz.pokebowl.repository.CardRepository
import cz.pokebowl.repository.SeriesRepository
import cz.pokebowl.repository.SetRepository
import cz.pokebowl.service.TCGDexService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.SerieResume
import net.tcgdex.sdk.models.SetResume
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
        }
    }

    single { TCGdex("en") }

    single { 
        ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .addMixIn(SetResume::class.java, TCGDexMixin::class.java)
            .addMixIn(Card::class.java, TCGDexMixin::class.java)
            .addMixIn(SerieResume::class.java, TCGDexMixin::class.java)
            .addMixIn(net.tcgdex.sdk.models.Set::class.java, TCGDexMixin::class.java)
    }

    single { SeriesRepository() }
    single { SetRepository() }
    single { CardRepository() }

    single { TCGDexService(get(), get(), get(), get(), get(), get()) }
}

