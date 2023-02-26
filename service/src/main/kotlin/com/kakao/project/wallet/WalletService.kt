package com.kakao.project.wallet

import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.exception.InsufficientBalanceException
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class WalletService(
    private val walletTransactionService: WalletTransactionService,
    private val walletRepository: WalletRepository
) {

    fun getWalletInfoByIdOrElseThrow(id: Long): WalletInfo {
        val wallet = (walletRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Not found Wallet. walletId: $id"))
        return WalletInfo(wallet)

    }

    @Transactional
    fun getWalletByIdOrElseThrowWithLock(walletId: Long): Wallet {
        return (walletRepository.findByIdWithLock(walletId)
            ?: throw EntityNotFoundException("Not found Wallet. walletId: $walletId"))
    }

    @Transactional
    fun createWallet(): WalletInfo {
        return WalletInfo(walletRepository.save(Wallet()))
    }

    @Transactional
    fun charge(walletId: Long, charge: Long): WalletInfo {
        logger.info { "charge $charge to wallet. walletId: $walletId" }

        val wallet = walletRepository.findByIdWithLock(walletId)
            ?: throw EntityNotFoundException("Not found Wallet. walletId: $walletId")
        wallet.balance += charge
        walletTransactionService.charge(wallet.identifier, charge)
        return WalletInfo(wallet)
    }


    @Transactional
    fun withdraw(walletId: Long, withdraw: Long): WalletInfo {
        val wallet = walletRepository.findByIdWithLock(walletId)
            ?: throw EntityNotFoundException("Not found Wallet. walletId: $walletId")
        if (wallet.balance < withdraw)
            throw InsufficientBalanceException("Insufficient Balance. balance: ${wallet.balance}, withdraw: $withdraw")
        wallet.balance -= withdraw
        walletTransactionService.withdraw(wallet.identifier, withdraw)
        return WalletInfo(wallet)
    }
}

data class WalletInfo(
    val walletId: Long,
    val balance: Long
) {
    companion object {
        operator fun invoke(wallet: Wallet) = with(wallet) {
            WalletInfo(
                walletId = wallet.identifier,
                balance = balance
            )
        }
    }

}