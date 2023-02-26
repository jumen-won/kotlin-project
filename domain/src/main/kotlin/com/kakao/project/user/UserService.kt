//package com.kakao.project.user
//
//import com.kakao.project.wallet.WalletService
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//
//@Transactional(readOnly = true)
//@Service
//class UserService(
//    private val walletService: WalletService,
//    private val userRepository: UserRepository,
//) {
//
//    @Transactional
//    fun create(name: String): User {
//        val user = userRepository.save(User(name))
//        walletService.create(user.id)
//        return user
//    }
//}