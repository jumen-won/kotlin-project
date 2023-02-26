package com.kakao.project.kafka.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakao.project.dto.DataLogEvent
import com.kakao.project.properties.KafkaProperties
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaProducerException
import org.springframework.kafka.core.KafkaSendCallback
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class DataLogEventKafkaProducer(
    properties: KafkaProperties,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val DATA_LOG_EVENT_PRODUCE_TOPIC = properties.topics[DATA_LOG_EVENT_TOPIC_KEY]!!

    fun sendMessage(event: DataLogEvent) {
        val message = objectMapper.writeValueAsString(event)
        sendWithCallback(DATA_LOG_EVENT_PRODUCE_TOPIC, message)
    }

    private fun sendWithCallback(topic: String, message: String) {
        val listenableFuture = kafkaTemplate.send(topic, message)
        listenableFuture.addCallback(listenableFutureCallback(message))
    }

    private fun listenableFutureCallback(message: String) =
        object : KafkaSendCallback<String, String> {
            override fun onSuccess(result: SendResult<String, String>?) {
                logger.info("Send Message = [ $message ] with offset=[ ${result!!.recordMetadata.offset()} ]")
            }

            override fun onFailure(ex: KafkaProducerException) {
                logger.error("Message 전달 오류 [ $message ] due to: ${ex.getFailedProducerRecord<String, String>()}")

            }
        }

    companion object {
        private const val DATA_LOG_EVENT_TOPIC_KEY = "DataLogEvent"
    }
}