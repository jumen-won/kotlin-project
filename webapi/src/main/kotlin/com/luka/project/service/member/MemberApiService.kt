package com.luka.project.service.member

import org.springframework.stereotype.Service

@Service
class MemberApiService(
    private val memberService : MemberService
) {
    fun getName(): String {
        return memberService.getName()
    }

    fun create() {
        memberService.create()
    }
}
