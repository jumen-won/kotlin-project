package com.kakao.project.service.purchase

import com.kakao.project.exception.InvalidValueException
import com.kakao.project.purchase.PurchaseInfo
import com.kakao.project.purchase.PurchaseService
import org.springframework.stereotype.Service

@Service
class PurchaseApiService(
    private val purchaseService: PurchaseService
) {
    fun purchase(productId: String, walletId: Long, quantity: Int): PurchaseInfo {
        validate(quantity)
        return purchaseService.purchase(productId, walletId, quantity)
    }

    private fun validate(quantity: Int) {
        if (quantity < 1) throw InvalidValueException("quantity not less than 1")
    }
}


