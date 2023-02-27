package com.kakao.project.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Coffee Order System API",
        description = "Coffee Order System API Description",
        version = "v1"
    )
)
class SwaggerConfig