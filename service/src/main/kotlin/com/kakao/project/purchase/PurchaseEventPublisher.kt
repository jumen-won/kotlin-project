package com.kakao.project.purchase

import com.kakao.project.dto.DataLogEvent
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class PurchaseEventPublisher(
    private val eventPublisher: ApplicationEventPublisher
) {

    fun publish(event: PurchaseCreatedEvent) {

        logger.info { "trying to publish PurchaseCreatedEvent. $event" }

        eventPublisher.publishEvent(event)
        publishDataLogEvent(DataLogEvent(event))
    }

    private fun publishDataLogEvent(event: DataLogEvent) {

        logger.info { "trying to publish DataLogEvent. $event" }

        eventPublisher.publishEvent(event)
    }
}