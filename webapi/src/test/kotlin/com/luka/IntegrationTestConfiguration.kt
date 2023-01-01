package com.luka

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
abstract class IntegrationTestConfiguration {
    // 해당 static 변수를 설정해야 컨테이너를 띄운다.
    // database 이름은 하기의 이름과 yml 설정에서 같게 만든다.
    companion object {
        @JvmStatic
        protected val dbContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("test-database")
        }

        init {
            dbContainer.start()
        }
    }
}