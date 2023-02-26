package com.kakao.project.wallet

import com.kakao.project.common.BaseEntity
import com.kakao.project.common.Identifiable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Wallet : BaseEntity(), Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0L
    var balance: Long = 0L
}