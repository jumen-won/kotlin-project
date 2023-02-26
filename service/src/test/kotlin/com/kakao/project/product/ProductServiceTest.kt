package com.kakao.project.product

import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.purchase.PurchaseEventService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
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
    fun getPopularProductList() {

        // case
        val days = cut.getStandardDays
        val size = cut.getStandardSize
        val dummyProductWithCountList = listOf(
            com.kakao.project.purchase.ProductWithCount("sample.coffee.1", 1000),
            com.kakao.project.purchase.ProductWithCount("sample.coffee.2", 100),
            com.kakao.project.purchase.ProductWithCount("sample.coffee.3", 10),
            com.kakao.project.purchase.ProductWithCount("sample.coffee.4", 100000),
            com.kakao.project.purchase.ProductWithCount("sample.coffee.5", 100000000),
        ).sortedByDescending { it.count }

        val now = LocalDate.now()

        for (before in 0 until days) {
            val targetDate = now.minusDays(before.toLong()).format(DateTimeFormatter.ISO_LOCAL_DATE)
            every {
                purchaseEventService.getProductWithCountListByDate(
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
            dummyProductWithCountList.take(size).maxBy { it.count }.count * days
        )
        assertEquals(
            popularProductList.last().count,
            dummyProductWithCountList.take(size).minBy { it.count }.count * days
        )

    }

    @Test
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
    fun `when not found product, throw Exception`() {

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