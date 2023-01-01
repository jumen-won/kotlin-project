package com.luka.project.service.member

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MemberApiServiceTest {

    private val memberService: MemberService = mockk()

    private lateinit var cut: MemberApiService

    @BeforeEach
    fun beforeEach() {
        cut = MemberApiService(memberService)
    }

    @Test
    fun getName() {
        val name = "luka"
        every { memberService.getName() } returns name
        assertEquals(name, cut.getName())
    }

}