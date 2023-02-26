package com.kakao.project.service.product

import com.kakao.project.product.PopularProductInfo
import com.kakao.project.product.ProductInfo
import com.kakao.project.product.ProductService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductApiServiceTest {
    private val productService: ProductService = mockk(relaxed = true)
    private lateinit var cut: ProductApiService

    @BeforeEach
    fun setup() {
        cut = ProductApiService(productService)
    }

    @Test
    fun getProductList() {

        // case
        val dummyProductList = listOf(
            dummyProductInfo("sample.coffee.1"),
            dummyProductInfo("sample.coffee.2"),
            dummyProductInfo("sample.coffee.3")
        )
        every { productService.getProductInfoList() } returns dummyProductList

        // when
        val productList = cut.getProductList()

        // then
        assertEquals(productList.size, dummyProductList.size)
    }

    @Test
    fun getPopularProductList() {

        // case
        val dummyPopularProductInfoLists = listOf(
            PopularProductInfo(dummyProductInfo("sample.coffee.1"), 10),
            PopularProductInfo(dummyProductInfo("sample.coffee.2"), 100),
            PopularProductInfo(dummyProductInfo("sample.coffee.3"), 1000)
        )
        every { productService.getPopularProductInfoList() } returns dummyPopularProductInfoLists

        // when
        val popularProductList = cut.getPopularProductList()

        // then
        assertEquals(popularProductList.size, dummyPopularProductInfoLists.size)
        assertEquals(popularProductList.first().count, dummyPopularProductInfoLists.maxByOrNull { it.count }!!.count)
    }

    private fun dummyProductInfo(productId: String): ProductInfo {
        return ProductInfo(
            productId = productId,
            title = "title",
            price = 1000L
        )
    }
}