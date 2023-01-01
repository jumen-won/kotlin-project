package com.luka.project.service.member

import com.luka.project.entity.member.Member
import com.luka.project.repository.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun getName(): String {
        val member = memberRepository.findAll().first()
        return member.name!!
    }

    @Transactional
    fun create() {
        val member = Member(name = "luka")
        memberRepository.save(member)
    }
}