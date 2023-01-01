import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {}
dependencies {}
project(":infrastructure") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    jar.enabled = true
    bootJar.enabled = false

    dependencies {
        runtimeOnly(Libs.h2)
        implementation(Libs.postgresql)
    }
}