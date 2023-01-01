plugins {}
dependencies {}
project(":webapi") {
    val jar: Jar by tasks
    val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
    jar.enabled = false
    bootJar.enabled = true

    dependencies {
        implementation(project(":domain"))

        //spring boot
        implementation(Libs.spring_boot_starter("web")) {
            exclude(Libs.spring_boot, "spring-boot-starter-tomcat")
        }

        implementation(Libs.spring_boot_starter("undertow"))
        implementation(Libs.spring_boot_starter("security"))
        implementation(Libs.spring_boot_starter("data-jpa"))

        implementation("net.java.dev.jna:jna:5.7.0")

        //test
        testImplementation(Libs.spring_boot_starter_test) {
            exclude("org.junit.vintage", "junit-vintage-engine")
        }
        testImplementation(Libs.spring_security_test)
        testImplementation(Libs.mockk)

        implementation("org.testcontainers:junit-jupiter")
        implementation("org.testcontainers:postgresql")

        implementation("org.postgresql:postgresql")


    }
}