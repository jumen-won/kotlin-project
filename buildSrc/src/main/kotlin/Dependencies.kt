object Libs {
    // spring_boot
    val spring_boot = "org.springframework.boot"
    val spring_boot_starter = "org.springframework.boot:spring-boot-starter"
    fun spring_boot_starter(moduleName: String) = "$spring_boot_starter-$moduleName"
    val spring_boot_starter_test = "org.springframework.boot:spring-boot-starter-test"
    val spring_security_test = "org.springframework.security:spring-security-test"
    val annotation_processor = "org.springframework.boot:spring-boot-configuration-processor"

    // plugin_spring
    val plugin_spring = "org.jetbrains.kotlin.plugin.spring"
    val plugin_jpa = "org.jetbrains.kotlin.plugin.jpa"

    // jackson
    val jackson_module_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"

    // kotlin
    val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect"
    val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib"

    // mockk
    val mockk = "io.mockk:mockk:${Versions.mockk}"

    // testcontainers
    fun testcontainers(moduleName: String) = "org.testcontainers:$moduleName:${Versions.testcontainers}"
    val testcontainers_bom = "org.testcontainers:testcontainers-bom:${Versions.testcontainers}"

    // database
    val h2 = "com.h2database:h2"
    val postgresql = "org.postgresql:postgresql"

    // jna
    val jna = "net.java.dev.jna:jna:${Versions.jna}"

    val kotlin_logging_jvm = "io.github.microutils:kotlin-logging-jvm:${Versions.kotlin_logging_jvm}"

    val mysql_connector_java = "mysql:mysql-connector-java"

    val redisson = "org.redisson:redisson-spring-boot-starter:${Versions.redisson}"

    val spring_kafka = "org.springframework.kafka:spring-kafka"

    val kafka_clients = "org.apache.kafka:kafka-clients:${Versions.kafka_clients}"

    val commons_lang3 = "org.apache.commons:commons-lang3:${Versions.commons_lang3}"

    // springdoc
    val springdoc_openapi_data_rest = "org.springdoc:springdoc-openapi-data-rest:${Versions.springdoc_openapi}"
    val springdoc_openapi_ui = "org.springdoc:springdoc-openapi-ui:${Versions.springdoc_openapi}"
    val springdoc_openapi_kotlin = "org.springdoc:springdoc-openapi-kotlin:${Versions.springdoc_openapi}"


}

object Versions {
    val springdoc_openapi = "1.6.0"
    val commons_lang3 = "3.12.0"
    val kafka_clients = "2.8.0"
    val spring_boot = "2.7.7"
    val spring_dependency_management = "1.0.15.RELEASE"
    val jvm = "1.8.0"
    val plugin_spring = "1.7.22"
    val plugin_jpa = "1.7.22"
    val kapt = "1.7.22"
    val mockk = "1.12.0"
    val testcontainers = "1.17.2"
    val jna = "5.7.0"
    val kotlin_logging_jvm = "2.1.23"
    val redisson = "3.17.7"
}

object Plugins {
    val spring_boot = "org.springframework.boot"
    val jvm = "jvm"
    val plugin_spring = "plugin.spring"
    val plugin_jpa = "plugin.jpa"
    val kapt = "kapt"
    val kotlin = "kotlin"

    val spring_dependency_management = "io.spring.dependency-management"
}