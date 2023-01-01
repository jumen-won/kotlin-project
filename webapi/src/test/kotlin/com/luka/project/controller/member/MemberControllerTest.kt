package com.luka.project.controller.member

import com.luka.project.service.member.MemberApiService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController::class)
class MemberControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var memberApiService: MemberApiService

    @Test
    @WithMockUser(username = "luka", password = "1234")
    fun get() {
        given(memberApiService.getName())
            .willReturn("luka")
        mockMvc.perform(get("/api/member"))
            .andExpect(status().isOk)
    }
}