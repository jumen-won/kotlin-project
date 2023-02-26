package com.kakao.project.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration


@Testcontainers
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
class IntegrationTestConfig {
    companion object {
        private const val MYSQL_DOCKER_IMAGE_NAME = "mysql:8"
        private const val MYSQL_DB_NAME = "kakao"
        private const val REDIS_DOCKER_IMAGE_NAME = "redis:alpine"
        private const val REDIS_EXPOSED_PORT = 6379
        private val topics = listOf(
            NewTopic("DataLogEvent", 1, 1)
        )

        @JvmStatic
        val mySQLContainer = MySQLContainer(MYSQL_DOCKER_IMAGE_NAME)
            .withDatabaseName(MYSQL_DB_NAME)
            .withInitScript("db/initdb.d/init.sql")
            .withStartupTimeout(Duration.ofMinutes(3))

        @JvmStatic
        val redisContainer = GenericContainer(DockerImageName.parse(REDIS_DOCKER_IMAGE_NAME))
            .withExposedPorts(REDIS_EXPOSED_PORT)


        @JvmStatic
        val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .apply {
                start()
                AdminClient.create(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers))
                    .use { adminClient -> adminClient.createTopics(topics) }
            }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {

            // mySQLContainer
            registry.add("spring.datasource.url") { mySQLContainer.jdbcUrl }
            registry.add("spring.datasource.driver-class-name") { mySQLContainer.driverClassName }
            registry.add("spring.datasource.username") { mySQLContainer.username }
            registry.add("spring.datasource.password") { mySQLContainer.password }

            // redisContainer
            registry.add("spring.redis.host") { redisContainer.host }
            registry.add("spring.redis.port") { redisContainer.getMappedPort(REDIS_EXPOSED_PORT) }

            // kafkaContainer
            registry.add("spring.kafka.bootstrap-servers") { kafkaContainer.bootstrapServers }
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            mySQLContainer.start()
            redisContainer.start()
            kafkaContainer.start()
        }
    }

    fun clearRedisCache() {
        redisContainer.execInContainer("redis-cli", "flushall")
    }

}