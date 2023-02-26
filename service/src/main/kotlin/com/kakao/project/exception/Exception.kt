package com.kakao.project.exception

open class BusinessException : RuntimeException {
    private var errorCode: ErrorCode

    constructor(message: String?, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    fun getErrorCode(): ErrorCode {
        return errorCode
    }
}


class InsufficientBalanceException(message: String) : BusinessException(message, ErrorCode.INSUFFICIENT_BALANCE)
class InsufficientChargeMinimumConditionsException(message: String) : BusinessException(message,
    ErrorCode.INSUFFICIENT_CHARGE_MINIMUM_CONDITIONS
)

class EntityNotFoundException(message: String) : BusinessException(message, ErrorCode.ENTITY_NOT_FOUND)
class InvalidValueException : BusinessException {
    constructor(value: String?) : super(value, ErrorCode.INVALID_INPUT_VALUE)
    constructor(value: String?, errorCode: ErrorCode?) : super(
        value, errorCode!!
    )
}


