import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {}
dependencies {}
project(":service") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    jar.enabled = true
    bootJar.enabled = false

    dependencies {
        implementation(project(":domain"))
        implementation(project(":infrastructure"))

        // test
        testImplementation(Libs.mockk)
        testImplementation(Libs.spring_boot_starter_test) { exclude("org.junit.vintage", "junit-vintage-engine") }
    }
}