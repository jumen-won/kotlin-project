package com.luka.project.repository

import com.luka.project.config.DomainConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(DomainConfiguration::class)
class DomainTestApplication