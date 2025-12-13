package cz.pokebowl

import cz.pokebowl.config.AppConfig
import cz.pokebowl.di.appModule
import cz.pokebowl.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    val appConfig = AppConfig(environment.config)

    install(Koin) {
        modules(appModule)
        modules(module {
            single { appConfig }
        })
    }

    configureMonitoring()
    configureSerialization()
    configureHTTP()
    configureStatusPages()
    configureDatabases(appConfig)
    configureRouting()
}
