package com.kakao.project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.kakao.project"])
class WebApiApplication

fun main(args: Array<String>) {
    runApplication<WebApiApplication>(*args)
}

