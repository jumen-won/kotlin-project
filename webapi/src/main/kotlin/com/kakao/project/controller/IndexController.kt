package com.kakao.project.controller

import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController(
    private val buildProperties: BuildProperties
) {

    @GetMapping("/index")
    fun index(): String {
        return "index"
    }

    @GetMapping("/buildInfo")
    fun buildInfo(): String {
        return "${buildProperties.group}.${buildProperties.artifact}.${buildProperties.name}:${buildProperties.version}"
    }
}