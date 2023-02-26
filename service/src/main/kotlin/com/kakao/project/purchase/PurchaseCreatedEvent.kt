package com.kakao.project.purchase

import java.time.LocalDate

data class PurchaseCreatedEvent(
    val walletId: Long,
    val walletTransactionId: Long,
    val productId: String,
    val quantity: Int,
    val price: Long,
    val totalPrice: Long,
    val purchasedAt: LocalDate
)