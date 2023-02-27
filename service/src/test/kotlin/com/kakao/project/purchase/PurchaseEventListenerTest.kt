package com.kakao.project.purchase

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PurchaseEventListenerTest {
    private val purchaseEventService: PurchaseEventService = mockk(relaxed = true)
    private lateinit var cut: PurchaseEventListener

    @BeforeEach
    fun setup() {
        cut = PurchaseEventListener(purchaseEventService)
    }

    @Test
    @DisplayName("주문 내역 이벤트가 발생한 경우 주문 횟수 카운트를 호출")
    fun handlePurchaseCreatedEvent() {
        // case
        val now = LocalDate.now()
        val quantity = 10
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

        val productIdSlot = slot<String>()
        val quantitySlot = slot<Int>()
        val purchasedAtSlot = slot<LocalDate>()
        every { purchaseEventService.count(capture(purchasedAtSlot), capture(productIdSlot), capture(quantitySlot)) } just runs

        // when
        cut.handlePurchaseCreatedEvent(dummyPurchaseCreatedEvent)

        // then
        verify(exactly = 1) { purchaseEventService.count(dummyPurchaseCreatedEvent.purchasedAt,dummyPurchaseCreatedEvent.productId, dummyPurchaseCreatedEvent.quantity) }
        assertEquals(purchasedAtSlot.captured, dummyPurchaseCreatedEvent.purchasedAt)
        assertEquals(productIdSlot.captured, dummyPurchaseCreatedEvent.productId)
        assertEquals(quantitySlot.captured, dummyPurchaseCreatedEvent.quantity)
    }
}