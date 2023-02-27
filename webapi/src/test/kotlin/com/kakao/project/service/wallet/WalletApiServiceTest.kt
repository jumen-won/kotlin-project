package com.kakao.project.service.wallet

import com.kakao.project.exception.InsufficientChargeMinimumConditionsException
import com.kakao.project.wallet.WalletInfo
import com.kakao.project.wallet.WalletService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyLong

class WalletApiServiceTest {

    private val walletService: WalletService = mockk(relaxed = true)
    private lateinit var walletApiService: WalletApiService

    @BeforeEach
    fun setup() {
        walletApiService = WalletApiService(walletService)
    }

    @Test
    @DisplayName("포인트 충전")
    fun charge() {
        val amount = 100000L
        val walletId = 1L
        val dummyWalletInfo = dummyWalletInfo(walletId, amount)
        every { walletService.charge(dummyWalletInfo.walletId, amount) } returns dummyWalletInfo
        assertDoesNotThrow {
            walletApiService.charge(dummyWalletInfo.walletId, amount)
        }
    }

    @Test
    @DisplayName("충전 금액이 최소 금액보다 적으면 에러 발생")
    fun charge_minimum_amount_condition() {
        val amount = 100L
        assertThrows<InsufficientChargeMinimumConditionsException> {
            walletApiService.charge(anyLong(), amount)
        }
    }

    @Test
    @DisplayName("충전 금액이 기준 단위가 아니면 에러 발생")
    fun charge_base_unit_condition() {
        val amount = 11000L
        assertThrows<InsufficientChargeMinimumConditionsException> {
            walletApiService.charge(anyLong(), amount)
        }
    }

    private fun dummyWalletInfo(walletId: Long, balance: Long): WalletInfo = WalletInfo(walletId, balance)
}