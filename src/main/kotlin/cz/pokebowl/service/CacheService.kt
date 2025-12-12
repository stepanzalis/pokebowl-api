package cz.pokebowl.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import redis.clients.jedis.JedisPool

class CacheService(
    @PublishedApi internal val jedisPool: JedisPool,
    @PublishedApi internal val ttlSeconds: Long = 300L
) {
    @PublishedApi internal val json = Json { ignoreUnknownKeys = true }

    inline fun <reified T> get(key: String): T? {
        return try {
            jedisPool.resource.use { jedis ->
                jedis.get(key)?.let { json.decodeFromString<T>(it) }
            }
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> set(key: String, value: T) {
        try {
            jedisPool.resource.use { jedis ->
                jedis.setex(key, ttlSeconds, json.encodeToString(value))
            }
        } catch (e: Exception) {
            // Log error but don't fail
        }
    }

    fun invalidate(key: String) {
        try {
            jedisPool.resource.use { jedis ->
                jedis.del(key)
            }
        } catch (e: Exception) {
            // Log error but don't fail
        }
    }

    fun invalidatePattern(pattern: String) {
        try {
            jedisPool.resource.use { jedis ->
                val keys = jedis.keys(pattern)
                if (keys.isNotEmpty()) {
                    jedis.del(*keys.toTypedArray())
                }
            }
        } catch (e: Exception) {
            // Log error but don't fail
        }
    }
}
