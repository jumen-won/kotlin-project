package com.kakao.project.wallet

import com.kakao.project.common.BaseEntity
import com.kakao.project.common.Identifiable
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class WalletTransaction(
    val walletId: Long,
    val amount: Long,
    @Enumerated(EnumType.STRING)
    val type: WalletTransactionType
) : BaseEntity(), Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0L
    val receipt : UUID = UUID.randomUUID()
}

