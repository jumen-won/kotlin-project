package com.kakao.project.controller.wallet

import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.wallet.WalletController.Companion.PATH_API_WALLETS
import com.kakao.project.service.wallet.WalletApiService
import com.kakao.project.wallet.WalletInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PATH_API_WALLETS)
class WalletController(
    private val walletApiService: WalletApiService
) {

    @Operation(summary = "지갑 생성 API", description = "지갑을 생성합니다.")
    @PostMapping("/create")
    fun createWallet(): WalletInfo {
        return walletApiService.create()
    }

    @Operation(summary = "지갑 조회 API", description = "지갑 정보를 조회합니다.")
    @GetMapping("/{walletId}")
    fun getWallet(@PathVariable walletId: String): WalletInfo {
        return walletApiService.getWalletInfo(walletId.toLong())
    }

    @Operation(summary = "포인트 충전 하기 API", description = "사용자 식별값, 충전금액을 입력 받아 포인트를 충전합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "400", description = "포인트 충전 최소금액 & 단위 조건 충족 실패"),
            ApiResponse(responseCode = "400", description = "존재하지 않는 지갑")
        ]
    )
    @PostMapping
    fun charge(@RequestBody request: WalletChargeRequest): WalletInfo {
        return walletApiService.charge(walletId = request.walletId, amount = request.amount)
    }

    companion object {
        private const val SERVICE_NAME = "wallet"
        const val PATH_API_WALLETS = "$PATH_API/$SERVICE_NAME"
    }
}

