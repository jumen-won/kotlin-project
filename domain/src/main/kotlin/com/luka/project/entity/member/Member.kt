package com.luka.project.entity.member

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member(
    @Id @GeneratedValue
    var id: Long? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null
)