package com.kakao.project.wallet

import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.exception.InsufficientBalanceException
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WalletServiceTest {
    private val walletTransactionService: WalletTransactionService = mockk(relaxed = true)
    private val walletRepository: WalletRepository = mockk(relaxed = true)

    private lateinit var walletService: WalletService

    @BeforeEach
    fun setup() {
        clearAllMocks()
        walletService = WalletService(walletTransactionService, walletRepository)
    }

    @Test
    fun create() {
        // case
        val dummyWallet = dummyWallet()
        every { walletRepository.save(any()) } returns dummyWallet

        // when
        val wallet = walletService.createWallet()

        // then
        assertEquals(wallet.walletId, dummyWallet.identifier)
    }

    @Test
    fun charge() {
        // case
        val dummyWallet = dummyWallet()
        val charge = 10000L
        assertEquals(0L, dummyWallet.balance)
        every { walletRepository.findByIdWithLock(dummyWallet.identifier) } returns dummyWallet
        every { walletTransactionService.charge(dummyWallet.identifier, charge) } just Runs

        // when
        val wallet = walletService.charge(dummyWallet.identifier, charge)

        // then
        assertEquals(wallet.balance, charge)
    }

    @Test
    fun `charge_when wallet entity not found, throw Exception`() {
        // case
        val dummyWallet = dummyWallet()
        val charge = 10000L

        assertEquals(0L, dummyWallet.balance)
        every { walletRepository.findByIdWithLock(dummyWallet.identifier) } returns null

        // when & then
        assertThrows<EntityNotFoundException> {
            walletService.charge(dummyWallet.identifier, charge)
        }
    }

    @Test
    fun withdraw() {
        // case
        val initialBalance = 10000L
        val withdraw = 5000L
        val dummyWallet = dummyWallet()
        dummyWallet.balance = initialBalance

        assertEquals(initialBalance, dummyWallet.balance)
        every { walletRepository.findByIdWithLock(dummyWallet.identifier) } returns dummyWallet


        val wallet = walletService.withdraw(dummyWallet.identifier, withdraw)
        assertEquals(initialBalance - withdraw, wallet.balance)
    }

    @Test
    fun `withdraw_when withdraw is bigger then balance, throw Exception`() {
        // case
        val dummyWallet = dummyWallet()
        val withdraw = 10000L
        assertEquals(0L, dummyWallet.balance)
        every { walletRepository.findByIdWithLock(dummyWallet.identifier) } returns dummyWallet

        // when & then
        assertThrows<InsufficientBalanceException> {
            walletService.withdraw(dummyWallet.identifier, withdraw)
        }

    }

    private fun dummyWallet(): Wallet = Wallet()
}