package com.kakao.project.listener

import com.kakao.project.dto.DataLogEvent
import com.kakao.project.kafka.producer.DataLogEventKafkaProducer
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

private val logger = KotlinLogging.logger {}

@Component
class DataLogEventListener(
    private val producer: DataLogEventKafkaProducer,
) {

    @TransactionalEventListener
    fun handle(event: DataLogEvent) {
        logger.info { "handle DataLogEvent. $event" }
        producer.sendMessage(event)
    }
}