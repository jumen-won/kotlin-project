package com.kakao.project.product

import com.kakao.project.exception.EntityNotFoundException
import com.kakao.project.purchase.PurchaseEventService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class ProductService(
    private val purchaseEventService: PurchaseEventService,
    private val productRepository: ProductRepository,
) {

    fun getProductInfoOrElseThrow(productId: String): ProductInfo {
        val product = (productRepository.findByProductId(productId)
            ?: throw EntityNotFoundException("Not found Product. productId: $productId"))
        return ProductInfo(product)
    }

    fun getProductInfoList(): List<ProductInfo> {
        val products = productRepository.findAll()
        return products.map { ProductInfo(it) }
    }

    fun getPopularProductInfoList(requestDate: LocalDate): List<PopularProductInfo> {

        val productScoreMap = mapOf<String, Long>().toMutableMap()

        for (before in 0 until getStandardDays) {
            val targetDate = requestDate.minusDays(before.toLong()).format(DateTimeFormatter.ISO_LOCAL_DATE)
            val productWithCountList =
                purchaseEventService.getProductCountPairListByDate(targetDate, getStandardSize.toLong())
            productWithCountList.forEach {
                productScoreMap.merge(it.first, it.second) { a, b -> a + b }
            }
        }

        val popularProductKeys =
            productScoreMap.entries.sortedByDescending { it.value }.take(getStandardSize).map { it.key }

        return popularProductKeys.map { productId ->
            PopularProductInfo(
                getProductInfoOrElseThrow(productId),
                productScoreMap[productId]!!
            )
        }
    }

    companion object {
        private const val STANDARD_DAYS = 7
        private const val STANDARD_SIZE = 3
    }

    val getStandardDays get() = STANDARD_DAYS
    val getStandardSize get() = STANDARD_SIZE
}

data class ProductInfo(
    val productId: String,
    val title: String,
    val price: Long
) {
    companion object {
        operator fun invoke(product: Product) = with(product) {
            ProductInfo(
                productId = productId,
                title = title,
                price = price
            )
        }
    }
}

data class PopularProductInfo(
    val productInfo: ProductInfo,
    val count: Long
) {
    companion object {
        operator fun invoke(productInfo: ProductInfo, count: Long) {
            PopularProductInfo(
                productInfo = productInfo,
                count = count
            )
        }
    }
}



