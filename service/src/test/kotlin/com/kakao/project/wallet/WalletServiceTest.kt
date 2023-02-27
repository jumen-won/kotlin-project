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
import org.junit.jupiter.api.DisplayName
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
    @DisplayName("지갑 생성 처리")
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
    @DisplayName("포인트 충전 처리")
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
    @DisplayName("포인트 결제 처리")
    fun withdraw() {
        // case
        val initialBalance = 10000L
        val withdraw = 5000L
        val dummyWallet = dummyWallet()
        dummyWallet.balance = initialBalance

        assertEquals(initialBalance, dummyWallet.balance)
        every { walletRepository.findByIdWithLock(dummyWallet.identifier) } returns dummyWallet

        // when & then
        val wallet = walletService.withdraw(dummyWallet.identifier, withdraw)
        assertEquals(initialBalance - withdraw, wallet.balance)
    }

    @Test
    @DisplayName("지갑이 존재하지 않는 경우 에러 발생")
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
    @DisplayName("잔액이 부족한 경우 에러 발생")
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