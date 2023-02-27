package com.kakao.project.controller.purchase

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakao.project.config.IntegrationTestConfig
import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.wallet.WalletChargeRequest
import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.exception.InsufficientBalanceException
import com.kakao.project.purchase.PurchaseInfo
import com.kakao.project.wallet.WalletInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


class PurchaseControllerTest(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) : IntegrationTestConfig() {

    @Test
    @DisplayName("커피 주문 정상 응답")
    fun purchase() {
        val productId = "coffee.1"
        val price = 1000L
        val quantity = 10
        val createWallet = createWallet()
        charge(createWallet.walletId, 10000L)
        val purchaseRequest =
            PurchaseRequest(productId = productId, walletId = createWallet.walletId, quantity = quantity)
        mockMvc.perform(
            MockMvcRequestBuilders.post("$PATH_API/purchase")
                .content(objectMapper.writeValueAsString(purchaseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect {
                val response = objectMapper.readValue(it.response.contentAsString, PurchaseInfo::class.java)
                assertEquals(createWallet.walletId, response.walletId)
                assertEquals(productId, response.productId)
                assertEquals(quantity, response.quantity)
                assertEquals(price * quantity, response.totalPrice)
            }
            .andDo { print() }
    }

    @Test
    @DisplayName("커피 주문 동시성 테스트")
    fun purchase_concurrency() {
        val createWallet = createWallet()
        val chargeAmount = 10000L
        val numberOfThreads = 13
        charge(createWallet.walletId, chargeAmount)
        val purchaseRequest = PurchaseRequest(productId = "coffee.1", walletId = createWallet.walletId, quantity = 1)

        val service = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        val atomicInteger = AtomicInteger(0)

        for (i in 0 until numberOfThreads) {
            service.execute {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("$PATH_API/purchase")
                        .content(objectMapper.writeValueAsString(purchaseRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                    .andExpect {
                        if (it.response.status == 400) atomicInteger.incrementAndGet()
                    }
                    .andDo { print() }
                latch.countDown()
            }
        }
        latch.await()

        assertEquals(3, atomicInteger.get())

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("$PATH_API/wallet/${createWallet.walletId}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                print()
            }.andReturn()
        val walletInfo = objectMapper.readValue(result.response.contentAsString, WalletInfo::class.java)
        assertEquals(0, walletInfo.balance)
    }

    @Test
    @DisplayName("존재하지 않는 상품 주문시 에러 발생")
    fun `when product not exists, throw Exception`() {
        val createWallet = createWallet()
        val purchaseRequest =
            PurchaseRequest(productId = "not.exists.product.id", walletId = createWallet.walletId, quantity = 1)
        mockMvc.perform(
            MockMvcRequestBuilders.post("$PATH_API/purchase")
                .content(objectMapper.writeValueAsString(purchaseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError)
            .andExpect {
                it.resolvedException.javaClass.isAssignableFrom(EntityNotFoundException::class.java)
            }
            .andDo { print() }
    }

    @Test
    @DisplayName("존재하지 않는 지갑인 경우 에러 발생")
    fun `when wallet not exists, throw Exception`() {
        val createWallet = createWallet()
        val purchaseRequest = PurchaseRequest(productId = "coffee.1", walletId = createWallet.walletId, quantity = 1)
        mockMvc.perform(
            MockMvcRequestBuilders.post("$PATH_API/purchase")
                .content(objectMapper.writeValueAsString(purchaseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError)
            .andExpect {
                it.resolvedException.javaClass.isAssignableFrom(EntityNotFoundException::class.java)
            }
            .andDo { print() }
    }

    @Test
    @DisplayName("잔액이 부족한 경우 에러 발생")
    fun `when balance less than totalPrice, throw Exception`() {
        val createWallet = createWallet()
        val purchaseRequest = PurchaseRequest(productId = "coffee.1", walletId = createWallet.walletId, quantity = 111)
        mockMvc.perform(
            MockMvcRequestBuilders.post("$PATH_API/purchase")
                .content(objectMapper.writeValueAsString(purchaseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError)
            .andExpect {
                it.resolvedException.javaClass.isAssignableFrom(InsufficientBalanceException::class.java)
            }
            .andDo { print() }
    }

    private fun createWallet(): WalletInfo {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("$PATH_API/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                print()
            }.andReturn()

        return objectMapper.readValue(result.response.contentAsString, WalletInfo::class.java)
    }

    private fun charge(walletId: Long, amount: Long) {
        val walletChargeRequest = WalletChargeRequest(walletId = walletId, amount = amount)
        mockMvc.perform(
            MockMvcRequestBuilders.post("$PATH_API/wallet")
                .content(objectMapper.writeValueAsString(walletChargeRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo { print() }
    }
}
