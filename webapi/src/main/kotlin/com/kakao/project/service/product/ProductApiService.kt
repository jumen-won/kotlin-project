package com.kakao.project.service.product

import com.kakao.project.product.PopularProductInfo
import com.kakao.project.product.ProductInfo
import com.kakao.project.product.ProductService
import org.springframework.stereotype.Service

@Service
class ProductApiService(
    private val productService: ProductService
) {
    fun getProductList(): List<ProductInfo> {
        return productService.getProductInfoList()
    }

    fun getPopularProductList(): List<PopularProductInfo> {
        return productService.getPopularProductInfoList().sortedByDescending { it.count }
    }

}