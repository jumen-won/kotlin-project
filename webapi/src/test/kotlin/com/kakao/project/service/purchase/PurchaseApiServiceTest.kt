package com.kakao.project.service.purchase

import com.kakao.project.exception.InvalidValueException
import com.kakao.project.purchase.PurchaseService
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
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
    fun purchase() {
        val quantity = 10
        assertDoesNotThrow { cut.purchase(anyString(), anyLong(), quantity) }
    }

    @Test
    fun `when quantity is less than 1, throw Exception`() {
        val quantity = 0
        assertThrows<InvalidValueException> { cut.purchase(anyString(), anyLong(), quantity) }
    }
}