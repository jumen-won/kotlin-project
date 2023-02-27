package com.kakao.project.controller.purchase

import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.purchase.PurchaseController.Companion.PATH_API_PURCHASE
import com.kakao.project.purchase.PurchaseInfo
import com.kakao.project.service.purchase.PurchaseApiService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PATH_API_PURCHASE)
class PurchaseController(
    private val purchaseApiService: PurchaseApiService
) {

    @Operation(summary = "커피 주문 하기 API", description = "커피를 주문하고 결제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "400", description = "최소 수량 조건 미달"),
            ApiResponse(responseCode = "400", description = "존재하지 않는 상품"),
            ApiResponse(responseCode = "400", description = "존재하지 않는 지갑"),
            ApiResponse(responseCode = "400", description = "잔액부족")
        ]
    )
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

