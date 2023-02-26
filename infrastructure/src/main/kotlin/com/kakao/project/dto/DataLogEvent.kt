package com.kakao.project.dto

data class DataLogEvent(
    val aggregateId: String,
    val createdAt: Long,
    val data: Any,
) {
    companion object {
        operator fun invoke(event: Any): DataLogEvent {
            return DataLogEvent(
                aggregateId = event::class.java.name,
                createdAt = System.currentTimeMillis(),
                data = event
            )
        }
    }
}