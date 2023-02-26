package com.kakao.project.service.wallet

import com.kakao.project.exception.InsufficientChargeMinimumConditionsException
import com.kakao.project.wallet.WalletInfo
import com.kakao.project.wallet.WalletService
import org.springframework.stereotype.Service


@Service
class WalletApiService(
    private val walletService: WalletService
) {

    fun create(): WalletInfo {
        return walletService.createWallet()
    }

    fun getWalletInfo(walletId: Long): WalletInfo {
        return walletService.getWalletInfoByIdOrElseThrow(walletId)
    }

    fun charge(walletId: Long, amount: Long): WalletInfo {
        validate(amount)
        return walletService.charge(walletId, amount)
    }

    private fun validate(amount: Long) {
        if (amount < MINIMUM_CHARGE_AMOUNT_WON || (amount % MINIMUM_CHARGE_AMOUNT_WON != 0L))
            throw InsufficientChargeMinimumConditionsException("Insufficient charging minimum conditions. amount: $amount")
    }

    companion object {
        private const val MINIMUM_CHARGE_AMOUNT_WON = 10000L
    }
}

