package com.luka

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
abstract class IntegrationTestConfiguration {
    companion object {
        @JvmStatic
        protected val dbContainer = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("kotlin-project")
        }

        init {
            dbContainer.start()
        }
    }
}