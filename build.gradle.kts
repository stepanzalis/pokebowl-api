import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.shadow)
}

group = "cz.stepanzalis"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    // --- Ktor Core ---
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.server.config.yaml)

    // --- Client ---
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)

    // --- Serialization ---
    implementation(libs.ktor.serialization.json)

    // --- DI (Koin) ---
    implementation(libs.koin)
    implementation(libs.koin.logger)

    // --- Database (Exposed + Postgres) ---
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)
    implementation(libs.postgresql)
    implementation(libs.hikari)

    // -- TCGDex
    implementation(libs.tcgdex)

    // --- Migrations (Flyway) ---
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)

    // Jackson (Required by Flyway sometimes, or useful for other JSON needs)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.kotlin)

    // --- Utils ---
    implementation(libs.logback.classic)
    implementation(libs.fuzzywuzzy)

    // --- Testing ---
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

tasks.withType<ShadowJar> {
    val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
    archiveFileName.set("app-$timestamp-all.jar")
    mergeServiceFiles()
}