package com.kakao.project.controller.wallet

import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.wallet.WalletController.Companion.PATH_API_WALLETS
import com.kakao.project.service.wallet.WalletApiService
import com.kakao.project.wallet.WalletInfo
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

    @PostMapping("/create")
    fun createWallet() : WalletInfo {
        return walletApiService.create()
    }

    @GetMapping("/{walletId}")
    fun getWallet(@PathVariable walletId: String) : WalletInfo {
        return walletApiService.getWalletInfo(walletId.toLong())
    }

    @PostMapping
    fun charge(@RequestBody request: WalletChargeRequest): WalletInfo {
        return walletApiService.charge(walletId = request.walletId, amount = request.amount)
    }

    companion object {
        private const val SERVICE_NAME = "wallet"
        const val PATH_API_WALLETS = "$PATH_API/$SERVICE_NAME"
    }
}

