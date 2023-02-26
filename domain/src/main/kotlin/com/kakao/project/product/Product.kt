package com.kakao.project.product

import com.kakao.project.common.BaseEntity
import com.kakao.project.common.Identifiable
import org.hibernate.annotations.Where
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Where(clause = "disabled = false")
class Product(
    var productId: String,
    var title: String,
    var price: Long,
    var disabled: Boolean? = false
) : BaseEntity(), Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0L
}