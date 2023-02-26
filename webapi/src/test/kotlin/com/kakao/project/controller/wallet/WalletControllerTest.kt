package com.kakao.project.controller.wallet

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakao.project.config.IntegrationTestConfig
import com.kakao.project.controller.PATH_API
import com.kakao.project.exception.InsufficientChargeMinimumConditionsException
import com.kakao.project.wallet.WalletInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class WalletControllerTest(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) : IntegrationTestConfig() {

    private fun createWallet(): WalletInfo {
        val result = mockMvc.perform(
            post("$PATH_API/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                print()
            }.andReturn()

        return objectMapper.readValue(result.response.contentAsString, WalletInfo::class.java)
    }

    @Test
    fun charge_concurrency() {
        val createWallet = createWallet()

        val chargeAmount = 10000L
        val numberOfThreads = 100
        val walletChargeRequest = WalletChargeRequest(walletId = createWallet.walletId, amount = chargeAmount)

        val service = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)
        for (i in 0 until numberOfThreads) {
            service.execute {
                mockMvc.perform(
                    post("$PATH_API/wallet")
                        .content(objectMapper.writeValueAsString(walletChargeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                    .andExpect(status().isOk)
                    .andDo {
                        print()
                    }
                latch.countDown()
            }
        }
        latch.await()

        val result = mockMvc.perform(
            get("$PATH_API/wallet/${createWallet.walletId}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                print()
            }.andReturn()
        val wallet = objectMapper.readValue(result.response.contentAsString, WalletInfo::class.java)
        assertEquals(chargeAmount * numberOfThreads, wallet.balance)
    }

    @Test
    fun charge() {
        val createWallet = createWallet()
        val walletChargeRequest = WalletChargeRequest(walletId = createWallet.walletId, amount = 10000L)
        mockMvc.perform(
            post("$PATH_API/wallet")
                .content(objectMapper.writeValueAsString(walletChargeRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                print()
            }
    }

    @Test
    fun `when amount is under minimum, throw Exception`() {
        val createWallet = createWallet()

        val walletChargeRequest = WalletChargeRequest(walletId = createWallet.walletId, amount = 1000L)
        mockMvc.perform(
            post("$PATH_API/wallet")
                .content(objectMapper.writeValueAsString(walletChargeRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError)
            .andExpect { it.resolvedException.javaClass.isAssignableFrom(InsufficientChargeMinimumConditionsException::class.java) }
            .andDo {
                print()
            }
    }
}