package com.kakao.project.purchase

import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}
typealias ProductCountPair = Pair<String, Long>

@Service
class PurchaseEventService(
    private val redisTemplate: RedisTemplate<String, Any>,
) {

    private val PURCHASE_DATE_KEY = "${PURCHASE_ZSET_KEY_PREFIX}${PURCHASE_ZSET_KEY_SUFFIX}"
    private val PRODUCT_MEMBER_KEY = "${PRODUCT_MEMBER_KEY_PREFIX}${PRODUCT_MEMBER_KEY_SUFFIX}"

    fun getProductCountPairListByDate(date: String, size: Long): List<ProductCountPair> {
        val zSet = redisTemplate.opsForZSet()
        val purchaseDateKey = getPurchaseDateKey(date)
        val tuples = zSet.reverseRangeWithScores(purchaseDateKey, 0, size - 1)!!
        return tuples.map {
            val productId = (it.value as String).replace(PRODUCT_MEMBER_KEY_PREFIX, "")
            val count = it.score!!.toLong()
            ProductCountPair(productId, count)
        }

    }

    fun count(date: LocalDate, productId: String, quantity: Int) {

        val zSet = redisTemplate.opsForZSet()
        val purchaseDateKey = getPurchaseDateKey(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
        val productIdMemberKey = getProductMemberKey(productId)

        logger.info { "trying to accumulate purchase count by productId. date: $date, productId: $productId, quantity: $quantity" }

        if (!redisTemplate.hasKey(purchaseDateKey)) {
            zSet.add(purchaseDateKey, productIdMemberKey, quantity.toDouble())
            redisTemplate.expire(purchaseDateKey, 1, TimeUnit.HOURS)
        } else {
            zSet.incrementScore(purchaseDateKey, productIdMemberKey, quantity.toDouble())
        }
    }


    private fun getPurchaseDateKey(date: String): String {
        return PURCHASE_DATE_KEY.replace(PURCHASE_ZSET_KEY_SUFFIX, date)
    }

    private fun getProductMemberKey(productId: String): String {
        return PRODUCT_MEMBER_KEY.replace(PRODUCT_MEMBER_KEY_SUFFIX, productId)
    }

    companion object {
        private const val PURCHASE_ZSET_KEY_PREFIX = "Purchase:"
        private const val PURCHASE_ZSET_KEY_SUFFIX = "{DATE}"
        private const val PRODUCT_MEMBER_KEY_PREFIX = "Product:"
        private const val PRODUCT_MEMBER_KEY_SUFFIX = "{PRODUCT_ID}"
    }
}