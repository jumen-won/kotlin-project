import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id(Libs.spring_boot) version Versions.spring_boot
    id(Plugins.spring_dependency_management) version Versions.spring_dependency_management
    kotlin(Plugins.jvm) version Versions.jvm
    kotlin(Plugins.kapt) version Versions.kapt
    kotlin(Plugins.plugin_spring) version Versions.plugin_spring
    kotlin(Plugins.plugin_jpa) version Versions.plugin_jpa

}

java.sourceCompatibility = JavaVersion.VERSION_17

subprojects {
    apply {
        plugin(Libs.spring_boot)
        plugin(Plugins.spring_dependency_management)
    }
}

springBoot {
    mainClass.set("com.kakao.WebApiApplicationKt")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks
jar.enabled = false
bootJar.enabled = false

allprojects {
    group = "com.kakao"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

}

subprojects {

    // kotlin
    apply(plugin = Plugins.kotlin)

    // spring
    apply(plugin = Plugins.spring_dependency_management)
    apply(plugin = Libs.spring_boot)
    apply(plugin = Libs.plugin_spring)
    apply(plugin = Libs.plugin_jpa)

    dependencies {
        implementation(Libs.jackson_module_kotlin)

        //kotlin
        implementation(Libs.kotlin_reflect)
        implementation(Libs.kotlin_stdlib)
        implementation(Libs.kotlin_logging_jvm)
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom(Libs.testcontainers_bom)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
