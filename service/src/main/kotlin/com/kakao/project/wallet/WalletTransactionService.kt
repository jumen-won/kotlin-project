package com.kakao.project.wallet

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WalletTransactionService(
    private val walletTransactionRepository: WalletTransactionRepository
) {

    @Transactional
    fun charge(walletId: Long, charge: Long) {
        walletTransactionRepository.save(
            WalletTransaction(
                walletId = walletId,
                amount = charge,
                type = WalletTransactionType.DEPOSIT
            )
        )
    }

    @Transactional
    fun withdraw(walletId: Long, withdraw: Long): WalletTransaction {
        return walletTransactionRepository.save(
            WalletTransaction(
                walletId = walletId,
                amount = withdraw,
                type = WalletTransactionType.WITHDRAW
            )
        )
    }
}