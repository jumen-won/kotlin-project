package com.kakao.project.product

import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.purchase.ProductCountPair
import com.kakao.project.purchase.PurchaseEventService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProductServiceTest {
    private val purchaseEventService: PurchaseEventService = mockk(relaxed = true)
    private val productRepository: ProductRepository = mockk(relaxed = true)
    private lateinit var cut: ProductService

    @BeforeEach
    fun setup() {
        cut = ProductService(purchaseEventService, productRepository)
    }

    @Test
    @DisplayName("인기메뉴 목록 조회")
    fun getPopularProductList() {

        // case
        val days = cut.getStandardDays
        val size = cut.getStandardSize
        val dummyProductWithCountList = listOf(
            ProductCountPair("sample.coffee.1", 1000),
            ProductCountPair("sample.coffee.2", 100),
            ProductCountPair("sample.coffee.3", 10),
            ProductCountPair("sample.coffee.4", 100000),
            ProductCountPair("sample.coffee.5", 100000000),
        ).sortedByDescending { it.second }

        val now = LocalDate.now()

        for (before in 0 until days) {
            val targetDate = now.minusDays(before.toLong()).format(DateTimeFormatter.ISO_LOCAL_DATE)
            every {
                purchaseEventService.getProductCountPairListByDate(
                    targetDate,
                    size.toLong()
                )
            } returns dummyProductWithCountList.take(size)
        }

        // when
        val popularProductList = cut.getPopularProductInfoList()

        // then
        assertEquals(popularProductList.size, size)
        assertEquals(
            popularProductList.first().count,
            dummyProductWithCountList.take(size).maxBy { it.second }.second * days
        )
        assertEquals(
            popularProductList.last().count,
            dummyProductWithCountList.take(size).minBy { it.second }.second * days
        )

    }

    @Test
    @DisplayName("상품 단건 조회")
    fun getProductOrElseThrow() {

        // case
        val productId = "sample.coffee.1"
        val dummyProduct = dummyProduct(productId)
        every { productRepository.findByProductId(dummyProduct.productId) } returns dummyProduct

        // when & then
        assertDoesNotThrow {
            val product = cut.getProductInfoOrElseThrow(dummyProduct.productId)
            assertEquals(product.productId, dummyProduct.productId)
        }
    }

    @Test
    @DisplayName("존재하지 않는 상품인 경우 에러 발생")
    fun getProductOrElseThrow_not_exists() {

        // case
        val productId = "wrong.product.id"
        every { productRepository.findByProductId(productId) } returns null

        // when & then
        assertThrows<EntityNotFoundException> { cut.getProductInfoOrElseThrow(productId) }
    }

    private fun dummyProduct(productId: String): Product {
        return Product(
            productId = productId,
            title = "title",
            price = 1000L
        )
    }
}