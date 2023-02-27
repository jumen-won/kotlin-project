package com.kakao.project.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController(
    private val buildProperties: BuildProperties
) {

    @GetMapping("/index")
    fun ping(): String {
        return "pong"
    }
    @Operation(summary = "빌드 정보 조회 API", description = "어플리케이션 빌드 정보를 조회합니다.")
    @GetMapping("/buildInfo")
    fun buildInfo(): String {
        return "${buildProperties.group}.${buildProperties.artifact}:${buildProperties.version}"
    }
}