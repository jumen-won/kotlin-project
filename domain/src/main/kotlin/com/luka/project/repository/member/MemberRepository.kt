package com.luka.project.repository.member

import com.luka.project.entity.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>