package com.kakao.project.exception

import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

data class ErrorResponse(
    val message: String,
    val status: Int,
    val errors: List<FieldError>,
    val code: String
) {
    constructor(code: ErrorCode, errors: List<FieldError> = emptyList()) : this(
        message = code.message,
        status = code.status,
        errors = errors,
        code = code.code
    )

    constructor(code: ErrorCode) : this(code, emptyList())

    constructor(e: MethodArgumentTypeMismatchException) : this(
        message = ErrorCode.INVALID_TYPE_VALUE.message,
        status = ErrorCode.INVALID_TYPE_VALUE.status,
        errors = FieldError.of(e.name, e.value?.toString() ?: "", e.errorCode),
        code = ErrorCode.INVALID_TYPE_VALUE.code
    )

    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult): ErrorResponse {
            return ErrorResponse(code, FieldError.of(bindingResult))
        }

        fun of(code: ErrorCode, errors: List<FieldError> = emptyList()): ErrorResponse {
            return ErrorResponse(code, errors)
        }

        fun of(code: ErrorCode): ErrorResponse {
            return ErrorResponse(code)
        }

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse {
            val errors = FieldError.of(e.name, e.value?.toString() ?: "", e.errorCode)
            return ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors)
        }
    }

    data class FieldError(
        val field: String,
        val value: String,
        val reason: String
    ) {
        companion object {
            fun of(field: String, value: String, reason: String): List<FieldError> {
                return listOf(FieldError(field, value, reason))
            }

            fun of(bindingResult: BindingResult): List<FieldError> {
                return bindingResult.fieldErrors.map { error ->
                    FieldError(
                        error.field,
                        error.rejectedValue?.toString() ?: "",
                        error.defaultMessage ?: ""
                    )
                }
            }
        }
    }
}