package com.kakao.project.service.product

import com.kakao.project.product.PopularProductInfo
import com.kakao.project.product.ProductInfo
import com.kakao.project.product.ProductService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProductApiService(
    private val productService: ProductService
) {
    fun getProductList(): List<ProductInfo> {
        return productService.getProductInfoList()
    }

    fun getPopularProductList(requestDate: LocalDate): List<PopularProductInfo> {
        return productService.getPopularProductInfoList(requestDate).sortedByDescending { it.count }
    }

}