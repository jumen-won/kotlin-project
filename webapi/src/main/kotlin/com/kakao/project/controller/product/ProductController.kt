package com.kakao.project.controller.product

import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.product.ProductController.Companion.PATH_API_PRODUCTS
import com.kakao.project.service.product.ProductApiService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PATH_API_PRODUCTS)
class ProductController(
    private val productApiService: ProductApiService
) {

    @GetMapping
    fun getProductList(): ProductListResponse {

        return ProductListResponse(productApiService.getProductList())
    }

    @GetMapping("/popular")
    fun getPopularProductList(): PopularProductListResponse {
        return PopularProductListResponse(productApiService.getPopularProductList())
    }

    companion object {
        private const val SERVICE_NAME = "products"
        const val PATH_API_PRODUCTS = "$PATH_API/$SERVICE_NAME"
    }
}


