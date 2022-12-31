import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {}
dependencies {}
project(":domain") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    jar.enabled = true
    bootJar.enabled = false

    dependencies {
        implementation(project(":infrastructure"))
        implementation(Libs.spring_boot_starter("data-jpa"))
    }
}
