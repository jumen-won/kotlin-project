package com.kakao.project.controller.wallet

data class WalletChargeRequest(
    val walletId: Long,
    val amount: Long
)