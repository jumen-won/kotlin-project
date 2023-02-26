package com.kakao.project.controller.purchase

import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.purchase.PurchaseController.Companion.PATH_API_PURCHASE
import com.kakao.project.purchase.PurchaseInfo
import com.kakao.project.service.purchase.PurchaseApiService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PATH_API_PURCHASE)
class PurchaseController(
    private val purchaseApiService: PurchaseApiService
) {

    @PostMapping
    fun purchase(@RequestBody request: PurchaseRequest): PurchaseInfo {
        return purchaseApiService.purchase(
            productId = request.productId,
            walletId = request.walletId,
            quantity = request.quantity
        )

    }

    companion object {
        private const val SERVICE_NAME = "purchase"
        const val PATH_API_PURCHASE = "$PATH_API/$SERVICE_NAME"
    }
}

