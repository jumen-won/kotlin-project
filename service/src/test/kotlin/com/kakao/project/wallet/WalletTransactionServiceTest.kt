package com.kakao.project.wallet

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WalletTransactionServiceTest {
    private val walletTransactionRepository: com.kakao.project.wallet.WalletTransactionRepository = mockk(relaxed = true)
    private lateinit var cut: WalletTransactionService

    @BeforeEach
    fun setup() {
        cut = WalletTransactionService(walletTransactionRepository)
    }

    @Test
    fun charge() {
        // case
        val walletId = 1L
        val charge = 100000L
        val walletTransactionType = WalletTransactionType.DEPOSIT
        val dummyWalletTransaction = WalletTransaction(walletId, charge, walletTransactionType)

        val slot = slot<WalletTransaction>()
        every { walletTransactionRepository.save(capture(slot)) } returns dummyWalletTransaction

        // when
        cut.charge(walletId, charge)

        // then
        val captured = slot.captured
        assertEquals(walletId, captured.walletId)
        assertEquals(charge, captured.amount)
        assertEquals(walletTransactionType, captured.type)

    }

    @Test
    fun withdraw() {
        // case
        val walletId = 1L
        val withdraw = 100000L
        val walletTransactionType = WalletTransactionType.WITHDRAW
        val dummyWalletTransaction =
            WalletTransaction(walletId, withdraw, walletTransactionType)

        val slot = slot<WalletTransaction>()
        every { walletTransactionRepository.save(capture(slot)) } returns dummyWalletTransaction

        // when
        cut.withdraw(walletId, withdraw)

        // then
        val captured = slot.captured
        assertEquals(walletId, captured.walletId)
        assertEquals(withdraw, captured.amount)
        assertEquals(walletTransactionType, captured.type)
    }
}