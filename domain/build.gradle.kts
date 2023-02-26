import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {}
project(":domain") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    jar.enabled = true
    bootJar.enabled = false

    dependencies {
//        implementation(Libs.spring_boot_starter("data-redis"))
        api(Libs.spring_boot_starter("data-jpa"))
        api(Libs.spring_boot_starter("data-redis"))

        // test
        testImplementation(Libs.mockk)
    }
}
