package com.kakao.project.purchase

import com.kakao.project.common.BaseEntity
import com.kakao.project.common.Identifiable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Purchase(
    val walletId: Long,
    val walletTransactionId: Long,
    val productId: String
) : BaseEntity(), Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0L
}