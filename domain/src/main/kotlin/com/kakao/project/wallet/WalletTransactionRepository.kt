package com.kakao.project.wallet

import org.springframework.data.jpa.repository.JpaRepository

interface WalletTransactionRepository : JpaRepository<WalletTransaction, Long>