package com.kakao.project.common

interface Identifiable<T> {
    val id: T
    val identifier: T
        get() = id
}