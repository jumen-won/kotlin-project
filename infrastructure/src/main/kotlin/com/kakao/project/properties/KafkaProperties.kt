package com.kakao.project.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "spring.kafka")
@ConstructorBinding
data class KafkaProperties(
    val bootstrapServers: String,
    val topics: Map<String, String>
)


