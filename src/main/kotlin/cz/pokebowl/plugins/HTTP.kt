package cz.pokebowl.plugins

import cz.pokebowl.config.RateLimitConfig.Companion.GLOBAL_MAX_REQUESTS
import cz.pokebowl.config.RateLimitConfig.Companion.PROTECTED_MAX_REQUESTS
import cz.pokebowl.config.RateLimitConfig.Companion.PROTECTED_RATE_LIMIT
import cz.pokebowl.config.RateLimitConfig.Companion.PROTECTED_REFILL_PERIOD
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*

fun Application.configureHTTP() {
    install(RateLimit) {
        register(RateLimitName(PROTECTED_RATE_LIMIT)) {
            rateLimiter(
                limit = PROTECTED_MAX_REQUESTS,
                refillPeriod = PROTECTED_REFILL_PERIOD
            )
        }
        global {
            rateLimiter(
                limit = GLOBAL_MAX_REQUESTS,
                refillPeriod = PROTECTED_REFILL_PERIOD
            )
        }
    }
}

