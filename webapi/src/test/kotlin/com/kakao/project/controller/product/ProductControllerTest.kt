package com.kakao.project.controller.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakao.project.config.IntegrationTestConfig
import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.purchase.PurchaseRequest
import com.kakao.project.controller.wallet.WalletChargeRequest
import com.kakao.project.purchase.PurchaseEventService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class ProductControllerTest(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val purchaseEventService: PurchaseEventService
) : IntegrationTestConfig() {

    @BeforeEach
    fun clear() {
        clearRedisCache()
    }

    @Test
    fun `update popular products live`() {

        // case
        val now = LocalDate.now()
        val days = 10
        for (before in 0..days) {
            val targetDate = now.minusDays(before.toLong())
            purchaseEventService.count(targetDate, "coffee.${before + 1}", (before + 1)  )
        }

        mockMvc.perform(
            get("$PATH_API/products/popular")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect {
                val response =
                    objectMapper.readValue(it.response.contentAsString, PopularProductListResponse::class.java)
                val products = response.products
                products.forEach { product -> println(product) }
                assertEquals(3, products.size)
                assertEquals(7, products.first().count)
                assertEquals(5, products.last().count)
            }
            .andDo { print() }
            .andReturn()

        purchaseEventService.count(now, "coffee.9", 90000)
        purchaseEventService.count(now, "coffee.10", 100000)
        purchaseEventService.count(now, "coffee.11", 110000)

        // when
        mockMvc.perform(
            get("$PATH_API/products/popular")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isOk)
            .andExpect {
                val response =
                    objectMapper.readValue(it.response.contentAsString, PopularProductListResponse::class.java)
                val products = response.products
                products.forEach { product -> println(product) }
                assertEquals(3, products.size)
                assertEquals(110000, products.first().count)
                assertEquals(90000, products.last().count)
            }
            .andDo { print() }
            .andReturn()
    }

    @Test
    fun getProductList() {
        // case
        val totalProductSize = 12

        // when
        mockMvc.perform(
            get("$PATH_API/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isOk)
            .andExpect {
                val response = objectMapper.readValue(it.response.contentAsString, ProductListResponse::class.java)
                assertEquals(totalProductSize, response.products.size)
            }
            .andDo {
                print()
            }.andReturn()
    }

    @Test
    fun getPopularProductList() {
        // case
        charge(100000L)
        for (i in 1..3) {
            purchase(i, i)
        }

        // when
        mockMvc.perform(
            get("$PATH_API/products/popular")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isOk)
            .andExpect {
                val response =
                    objectMapper.readValue(it.response.contentAsString, PopularProductListResponse::class.java)
                val products = response.products
                products.forEach { product -> println(product) }
                assertEquals(3, products.size)
                assertEquals(3, products.first().count)
                assertEquals(1, products.last().count)
            }
            .andDo { print() }
            .andReturn()


    }

    private fun purchase(index: Int, quantity: Int) {
        val purchaseRequest = PurchaseRequest(productId = "coffee.$index", walletId = 1L, quantity = quantity)
        mockMvc.perform(
            post("$PATH_API/purchase")
                .content(objectMapper.writeValueAsString(purchaseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo { print() }
    }

    private fun charge(amount: Long) {
        val walletChargeRequest = WalletChargeRequest(walletId = 1L, amount = amount)
        mockMvc.perform(
            post("$PATH_API/wallet")
                .content(objectMapper.writeValueAsString(walletChargeRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo { print() }
    }
}