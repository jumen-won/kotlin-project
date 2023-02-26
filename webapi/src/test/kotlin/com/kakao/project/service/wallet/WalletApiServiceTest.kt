package com.kakao.project.service.wallet

import com.kakao.project.exception.InsufficientChargeMinimumConditionsException
import com.kakao.project.wallet.WalletInfo
import com.kakao.project.wallet.WalletService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
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
    fun `충전금액이 최소금액보다 적으면 에러발생`() {
        val amount = 100L
        assertThrows<InsufficientChargeMinimumConditionsException> {
            walletApiService.charge(anyLong(), amount)
        }
    }

    @Test
    fun `충전금액이 기준단위가 아니면 에러발생`() {
        val amount = 11000L
        assertThrows<InsufficientChargeMinimumConditionsException> {
            walletApiService.charge(anyLong(), amount)
        }
    }

    private fun dummyWalletInfo(walletId: Long, balance: Long): WalletInfo = WalletInfo(walletId, balance)
}