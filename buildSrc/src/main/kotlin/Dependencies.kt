object Libs {
    // spring_boot
    val spring_boot = "org.springframework.boot"
    val spring_boot_starter = "org.springframework.boot:spring-boot-starter"
    fun spring_boot_starter(moduleName: String) = "$spring_boot_starter-$moduleName"
    val spring_boot_starter_test = "org.springframework.boot:spring-boot-starter-test"
    val spring_security_test = "org.springframework.security:spring-security-test"

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
    fun testcontainers(moduleName: String) = "org.testcontainers:$moduleName"
    val testcontainers_bom = "org.testcontainers:testcontainers-bom:${Versions.testcontainers}"

    // database
    val h2 = "com.h2database:h2"
    val postgresql = "org.postgresql:postgresql"

    // jna
    val jna = "net.java.dev.jna:jna:${Versions.jna}"

}

object Versions {
    val spring_boot = "2.7.7"
    val spring_dependency_management = "1.0.15.RELEASE"
    val jvm = "1.8.0"
    val plugin_spring = "1.7.22"
    val plugin_jpa = "1.7.22"
    val kapt = "1.7.22"
    val mockk = "1.12.0"
    val testcontainers = "1.15.1"
    val jna = "5.7.0"
}

object Plugins {
    val jvm = "jvm"
    val plugin_spring = "plugin.spring"
    val plugin_jpa = "plugin.jpa"
    val kapt = "kapt"
    val kotlin = "kotlin"

    val spring_dependency_management = "io.spring.dependency-management"
}