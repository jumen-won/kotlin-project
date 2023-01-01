package com.luka.project.repository

import com.luka.project.config.DomainConfiguration
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootConfiguration
@Import(DomainConfiguration::class, TestConfiguration::class)
class DomainTestApplication

@Testcontainers
class TestConfiguration {
    companion object {
        @Container
        @JvmStatic
        private val dbContainer = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("kotlin-project")

        }

        init {
            dbContainer.start()
        }
    }
}