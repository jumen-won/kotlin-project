package com.luka.project.controller.member

import com.luka.project.service.member.MemberApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberApiService: MemberApiService
) {

    @PostMapping
    fun create() {
        memberApiService.create()
    }

    @GetMapping
    fun getName(): ResponseEntity<String> {
        return ResponseEntity.ok(memberApiService.getName())
    }
}