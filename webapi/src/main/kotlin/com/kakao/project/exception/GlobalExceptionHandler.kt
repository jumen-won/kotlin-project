package com.kakao.project.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


private val logger = KotlinLogging.logger {}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error(e) { "handleMethodArgumentNotValidException" }
        val response: ErrorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        logger.error(e) { "handleMethodArgumentTypeMismatchException" }
        val response: ErrorResponse = ErrorResponse.of(e)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        logger.error(e) { "handleHttpRequestMethodNotSupportedException" }
        val response: ErrorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED)
        return ResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        logger.error(e) { "handleBusinessException" }
        val errorCode = e.getErrorCode()
        val response: ErrorResponse = ErrorResponse.of(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }


    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e) { "handleException" }
        val response: ErrorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}