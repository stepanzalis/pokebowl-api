package cz.pokebowl

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        // Our health check is at /api/v1/health
        client.get("/api/v1/health").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
