package com.kakao.project.purchase

import com.kakao.project.dto.DataLogEvent
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDate

class PurchaseEventPublisherTest {
    private val eventPublisher: ApplicationEventPublisher = mockk(relaxed = true)
    private lateinit var cut: PurchaseEventPublisher

    @BeforeEach
    fun setup() {
        cut = PurchaseEventPublisher(eventPublisher)
    }

    @Test
    @DisplayName("주문 이벤트 및 데이터 수집 이벤트 생성")
    fun publish() {

        // case
        val now = LocalDate.now()
        val quantity = 1
        val price = 1000L
        val dummyPurchaseCreatedEvent = PurchaseCreatedEvent(
            walletId = 1L,
            walletTransactionId = 11L,
            productId = "sample.coffee.1",
            quantity = quantity,
            price = price,
            purchasedAt = now,
            totalPrice = quantity * price
        )

        val slots = mutableListOf<Any>()

        // when
        cut.publish(dummyPurchaseCreatedEvent)

        // then
        verify(exactly = 2) { eventPublisher.publishEvent(capture(slots)) }
        val purchaseCreatedEvent = slots.first() as PurchaseCreatedEvent
        assertEquals(purchaseCreatedEvent.walletId, dummyPurchaseCreatedEvent.walletId)
        assertEquals(purchaseCreatedEvent.walletTransactionId, dummyPurchaseCreatedEvent.walletTransactionId)
        assertEquals(purchaseCreatedEvent.productId, dummyPurchaseCreatedEvent.productId)
        assertEquals(purchaseCreatedEvent.quantity, dummyPurchaseCreatedEvent.quantity)
        assertEquals(purchaseCreatedEvent.price, dummyPurchaseCreatedEvent.price)
        assertEquals(purchaseCreatedEvent.totalPrice, dummyPurchaseCreatedEvent.totalPrice)
        assertEquals(purchaseCreatedEvent.purchasedAt, dummyPurchaseCreatedEvent.purchasedAt)

        val dataLogEvent = slots.last() as DataLogEvent
        assertEquals(dataLogEvent.data as PurchaseCreatedEvent, purchaseCreatedEvent)
    }
}