plugins {}
project(":webapi") {
    val jar: Jar by tasks
    val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
    jar.enabled = false
    bootJar.enabled = true

    dependencies {
        implementation(project(":service"))

        implementation(Libs.spring_boot_starter("web")) { exclude(Libs.spring_boot, "spring-boot-starter-tomcat") }
        implementation(Libs.spring_boot_starter("undertow"))

        implementation(Libs.springdoc_openapi_data_rest)
        implementation(Libs.springdoc_openapi_ui)
        implementation(Libs.springdoc_openapi_kotlin)

        testImplementation(Libs.spring_boot_starter_test) { exclude("org.junit.vintage", "junit-vintage-engine") }
        testImplementation(Libs.mockk)
        testImplementation(Libs.kafka_clients)
        testImplementation(Libs.commons_lang3)
        testImplementation(Libs.testcontainers("junit-jupiter"))
        testImplementation(Libs.testcontainers("testcontainers")) // mysql 컨테이너를 사용한다면 추가
        testImplementation(Libs.testcontainers("mysql"))
        testImplementation(Libs.testcontainers("kafka"))
    }
}

springBoot {
    buildInfo()
}
