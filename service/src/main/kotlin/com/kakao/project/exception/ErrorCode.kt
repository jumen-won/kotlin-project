package com.kakao.project.exception

enum class ErrorCode(val status: Int, val code: String, val message: String) {
    // Common
    INVALID_INPUT_VALUE(400, "C0001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C0002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "C0003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C0004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C0005", " Invalid Type Value"),

    // Purchase
    INSUFFICIENT_BALANCE(400, "P0001", "Insufficient Balance"),

    // Wallet
    INSUFFICIENT_CHARGE_MINIMUM_CONDITIONS(400, "W0001", "Insufficient charging minimum conditions")
    ;
}

