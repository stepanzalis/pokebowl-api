package cz.pokebowl.config

import kotlin.time.Duration.Companion.seconds

class RateLimitConfig {
    companion object {
        const val PROTECTED_RATE_LIMIT = "protected"

        const val PROTECTED_MAX_REQUESTS = 40
        const val GLOBAL_MAX_REQUESTS = 30

        val PROTECTED_REFILL_PERIOD = 60.seconds

    }
}