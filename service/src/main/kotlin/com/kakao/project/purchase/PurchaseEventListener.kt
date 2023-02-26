package com.kakao.project.purchase

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

private val logger = KotlinLogging.logger {}

@Component
class PurchaseEventListener(
    private val purchaseEventService: PurchaseEventService
) {
    @TransactionalEventListener
    fun handlePurchaseCreatedEvent(event: PurchaseCreatedEvent) {
        logger.info { "handle PurchaseCreatedEvent. $event" }
        purchaseEventService.count(event.purchasedAt, event.productId, event.quantity)

    }
}