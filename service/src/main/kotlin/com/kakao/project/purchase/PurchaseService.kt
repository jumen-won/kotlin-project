package com.kakao.project.purchase

import com.kakao.project.exception.InsufficientBalanceException
import com.kakao.project.product.ProductService
import com.kakao.project.wallet.WalletService
import com.kakao.project.wallet.WalletTransactionService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Transactional(readOnly = true)
@Service
class PurchaseService(
    private val walletService: WalletService,
    private val walletTransactionService: WalletTransactionService,
    private val productService: ProductService,
    private val eventPublisher: PurchaseEventPublisher,
    private val purchaseRepository: PurchaseRepository
) {

    @Transactional
    fun purchase(productId: String, walletId: Long, quantity: Int): PurchaseInfo {

        logger.info { "trying to purchase product. productId: $productId, walletId: $walletId" }

        val product = productService.getProductInfoOrElseThrow(productId)
        val wallet = walletService.getWalletByIdOrElseThrowWithLock(walletId)
        val totalPrice = (product.price) * quantity

        if (wallet.balance < totalPrice) throw InsufficientBalanceException("Insufficient Balance. balance: ${wallet.balance}, totalPrice: $totalPrice")

        walletService.withdraw(walletId, totalPrice)
        val walletTransaction = walletTransactionService.withdraw(walletId, totalPrice)

        val purchase = purchaseRepository.save(
            Purchase(
                walletId = wallet.identifier,
                productId = productId,
                walletTransactionId = walletTransaction.identifier
            )
        )

        val now = LocalDate.now()

        eventPublisher.publish(
            PurchaseCreatedEvent(
                walletId = wallet.identifier,
                walletTransactionId = walletTransaction.identifier,
                productId = purchase.productId,
                quantity = quantity,
                price = product.price,
                totalPrice = totalPrice,
                purchasedAt = now
            )
        )

        return PurchaseInfo(
            walletId = walletId,
            productId = productId,
            price = product.price,
            quantity = quantity,
            totalPrice = totalPrice,
            receipt = walletTransaction.receipt.toString(),
            purchaseAt = now
        )
    }
}

data class PurchaseInfo(
    val walletId: Long,
    val productId: String,
    val price : Long,
    val quantity: Int,
    val totalPrice: Long,
    val receipt: String,
    val purchaseAt: LocalDate
)