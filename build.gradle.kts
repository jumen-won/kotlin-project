import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id(Libs.spring_boot) version Versions.spring_boot
    id(Plugins.spring_dependency_management) version Versions.spring_dependency_management
    kotlin(Plugins.jvm) version Versions.jvm
    kotlin(Plugins.kapt) version Versions.kapt
    kotlin(Plugins.plugin_spring) version Versions.plugin_spring apply true
    kotlin(Plugins.plugin_jpa) version Versions.plugin_jpa apply true
}

java.sourceCompatibility = JavaVersion.VERSION_17


springBoot {
    mainClass.set("com.luka.WebApiApplicationKt")
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
    group = "com.luka"
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

        //spring boot
        implementation(Libs.spring_boot_starter("web")) {
            exclude("org.springframework.boot", "spring-boot-starter-tomcat")
        }

        implementation(Libs.h2_database)
        implementation(Libs.spring_boot_starter("undertow"))
        implementation(Libs.spring_boot_starter("security"))
        implementation(Libs.spring_boot_starter("data-jpa"))

        implementation(Libs.jackson_module_kotlin)

        //kotlin
        implementation(Libs.kotlin_reflect)
        implementation(Libs.kotlin_stdlib)

        //test
        testImplementation(Libs.spring_boot_starter_test)
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}