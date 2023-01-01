package com.luka.project.controller.member

import com.luka.IntegrationTestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class MemberIntegrationTest : IntegrationTestConfiguration() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(username = "luka", password = "1234")
    fun test() {
        mockMvc.perform(post("/api/member")
            .with(csrf()))
            .andExpect(status().isOk)

        mockMvc.perform(get("/api/member"))
            .andExpect(status().isOk)

    }
}