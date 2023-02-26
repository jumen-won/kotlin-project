package com.kakao.project.config

import com.kakao.project.properties.RedisProperties
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    private val properties: RedisProperties
) {
    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().address = "${REDIS_HOST_PREFIX}${properties.host}:${properties.port}"

        return Redisson.create(config)
    }

    @Bean
    fun redissonConnectionFactory(redisson: RedissonClient): RedissonConnectionFactory {
        return RedissonConnectionFactory(redisson)
    }

    @Bean
    fun redisTemplate(
        redissonClient: RedissonClient
    ): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            connectionFactory = redissonConnectionFactory(redissonClient)
        }
    }

    companion object {
        private const val REDIS_HOST_PREFIX = "redis://"
    }
}