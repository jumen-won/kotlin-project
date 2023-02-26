package com.kakao.project.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByProductId(productId: String): Product?

    @Query("select p from Product p where p.productId in :ids")
    fun getProductListByProductIds(ids: List<String>): List<Product>
}