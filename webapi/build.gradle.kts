plugins {}
dependencies {}
project(":webapi") {
    val jar: Jar by tasks
    val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
    jar.enabled = false
    bootJar.enabled = true

    dependencies {
        implementation(project(":domain"))
    }
}