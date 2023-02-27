package com.kakao.project.controller.product

import com.kakao.project.controller.PATH_API
import com.kakao.project.controller.product.ProductController.Companion.PATH_API_PRODUCTS
import com.kakao.project.service.product.ProductApiService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping(PATH_API_PRODUCTS)
class ProductController(
    private val productApiService: ProductApiService
) {

    @Operation(summary = "커피 메뉴 목록 조회 API", description = "커피 메뉴 목록을 반환합니다.")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
    @GetMapping
    fun getProductList(): ProductListResponse {
        return ProductListResponse(productApiService.getProductList())
    }

    @Operation(summary = "인기메뉴 목록 조회 API", description = "최근 7일간 인기있는 메뉴 3개를 조회합니다.")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
    @GetMapping("/popular")
    fun getPopularProductList(): PopularProductListResponse {
        val requestDate = LocalDate.now()
        return PopularProductListResponse(
            baseDate = requestDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
            popularProductInfoList = productApiService.getPopularProductList(requestDate)
        )
    }

    companion object {
        private const val SERVICE_NAME = "products"
        const val PATH_API_PRODUCTS = "$PATH_API/$SERVICE_NAME"
    }
}


