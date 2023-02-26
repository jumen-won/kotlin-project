package com.kakao.project.controller.purchase

data class PurchaseRequest(
    val productId: String,
    val walletId: Long,
    val quantity: Int = 1,
)