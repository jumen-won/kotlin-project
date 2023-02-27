package com.kakao.project.service.purchase

import com.kakao.project.exception.InvalidValueException
import com.kakao.project.purchase.PurchaseService
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString


class PurchaseApiServiceTest {
    private val purchaseService: PurchaseService = mockk(relaxed = true)
    private lateinit var cut: PurchaseApiService

    @BeforeEach
    fun setup() {
        cut = PurchaseApiService(purchaseService)
    }

    @Test
    @DisplayName("주문 요청")
    fun purchase() {
        val quantity = 10
        assertDoesNotThrow { cut.purchase(anyString(), anyLong(), quantity) }
    }

    @Test
    @DisplayName("최소 수량 조건 미달시 에러 발생")
    fun `when quantity is less than 1, throw Exception`() {
        val quantity = 0
        assertThrows<InvalidValueException> { cut.purchase(anyString(), anyLong(), quantity) }
    }
}