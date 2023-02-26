package com.kakao.project.purchase

import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.exception.InsufficientBalanceException
import com.kakao.project.product.ProductInfo
import com.kakao.project.product.ProductService
import com.kakao.project.wallet.WalletService
import com.kakao.project.wallet.WalletTransactionService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString

class PurchaseServiceTest {
    private val walletService: WalletService = mockk(relaxed = true)
    private val walletTransactionService: WalletTransactionService = mockk(relaxed = true)
    private val productService: ProductService = mockk(relaxed = true)
    private val eventPublisher: PurchaseEventPublisher = mockk(relaxed = true)
    private val purchaseRepository: PurchaseRepository = mockk(relaxed = true)
    private lateinit var cut: PurchaseService

    @BeforeEach
    fun setup() {
        cut = PurchaseService(
            walletService = walletService,
            walletTransactionService = walletTransactionService,
            productService = productService,
            eventPublisher = eventPublisher,
            purchaseRepository = purchaseRepository
        )
    }

    @Test
    fun purchase() {

        // case
        val productId = "sample.coffee.1"
        val quantity = 10

        val dummyProductInfo = dummyProductInfo(productId)
        val dummyWallet = dummyWallet()
        dummyWallet.balance += 10000L
        val walletId = dummyWallet.identifier

        val totalPrice = dummyProductInfo.price * quantity

        every { productService.getProductInfoOrElseThrow(dummyProductInfo.productId) } returns dummyProductInfo
        every { walletService.getWalletByIdOrElseThrowWithLock(dummyWallet.identifier) } returns dummyWallet

        val dummyWalletTransaction = com.kakao.project.wallet.WalletTransaction(
            walletId = dummyWallet.identifier,
            amount = totalPrice,
            type = com.kakao.project.wallet.WalletTransactionType.WITHDRAW
        )

        every { walletTransactionService.withdraw(dummyWallet.identifier, totalPrice) } returns dummyWalletTransaction

        val dummyPurchase = Purchase(
            walletId = dummyWallet.identifier,
            productId = dummyProductInfo.productId,
            walletTransactionId = dummyWalletTransaction.identifier
        )
        val purchaseSlot = slot<Purchase>()
        every { purchaseRepository.save(capture(purchaseSlot)) } returns dummyPurchase

        val purchaseCreatedEventCapturingSlot = slot<PurchaseCreatedEvent>()
        every { eventPublisher.publish(capture(purchaseCreatedEventCapturingSlot)) } just runs

        // when
        assertDoesNotThrow {
            val purchaseInfo = cut.purchase(dummyProductInfo.productId, dummyWallet.identifier, quantity)
            assertEquals(dummyWallet.identifier, purchaseInfo.walletId)
            assertEquals(dummyProductInfo.productId, purchaseInfo.productId)
            assertEquals(dummyProductInfo.price, purchaseInfo.price)
            assertEquals(quantity, purchaseInfo.quantity)
            assertEquals(dummyProductInfo.price * quantity, purchaseInfo.totalPrice)
        }

        // then
        verify(exactly = 1) { walletService.withdraw(walletId, totalPrice) }
        verify(exactly = 1) { eventPublisher.publish(any()) }

        val capturedPurchase = purchaseSlot.captured
        assertEquals(capturedPurchase.identifier, dummyPurchase.identifier)
        assertEquals(capturedPurchase.walletId, dummyPurchase.walletId)
        assertEquals(capturedPurchase.productId, dummyPurchase.productId)
        assertEquals(capturedPurchase.walletTransactionId, dummyPurchase.walletTransactionId)


        val capturedPurchaseCreatedEvent = purchaseCreatedEventCapturingSlot.captured
        assertEquals(capturedPurchaseCreatedEvent.walletId, dummyWallet.identifier)
        assertEquals(capturedPurchaseCreatedEvent.walletTransactionId, dummyWalletTransaction.identifier)
        assertEquals(capturedPurchaseCreatedEvent.productId, dummyProductInfo.productId)
        assertEquals(capturedPurchaseCreatedEvent.price, dummyProductInfo.price)
        assertEquals(capturedPurchaseCreatedEvent.totalPrice, totalPrice)
    }

    @Test
    fun `when product not found, throw Exception`() {
        // case
        val productId = "wrong.product.id"
        every { productService.getProductInfoOrElseThrow(productId) } throws EntityNotFoundException("")

        // when & then
        assertThrows<EntityNotFoundException> { cut.purchase(productId, anyLong(), anyInt()) }
    }

    @Test
    fun `when wallet not found, throw Exception`() {
        // case
        val walletId = Long.MAX_VALUE
        every { walletService.getWalletByIdOrElseThrowWithLock(walletId) } throws EntityNotFoundException("")

        // when & then
        assertThrows<EntityNotFoundException> { cut.purchase(anyString(), walletId, anyInt()) }
    }

    @Test
    fun `when wallet's balance less than products's price, throw Exception`() {

        // case
        val quantity = 10
        val dummyWallet = dummyWallet()
        assertEquals(dummyWallet.balance, 0L)
        val productId = "sample.coffee.1"
        val dummyProductInfo = dummyProductInfo(productId)

        every { productService.getProductInfoOrElseThrow(dummyProductInfo.productId) } returns dummyProductInfo
        every { walletService.getWalletByIdOrElseThrowWithLock(dummyWallet.identifier) } returns dummyWallet

        // when & then
        assertThrows<InsufficientBalanceException> { cut.purchase(dummyProductInfo.productId, dummyWallet.identifier, quantity) }
    }

    private fun dummyWallet(): com.kakao.project.wallet.Wallet = com.kakao.project.wallet.Wallet()

    private fun dummyProductInfo(productId: String): ProductInfo {
        return ProductInfo(
            productId = productId,
            title = "title",
            price = 1000L
        )
    }
}