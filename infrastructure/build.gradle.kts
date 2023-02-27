import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {}
dependencies {}
project(":infrastructure") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    jar.enabled = true
    bootJar.enabled = false

    dependencies {
        implementation(Libs.mysql_connector_java)
        implementation(Libs.redisson)
        implementation(Libs.spring_kafka)
    }
}