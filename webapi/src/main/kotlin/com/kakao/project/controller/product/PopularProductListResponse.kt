package com.kakao.project.controller.product

import com.kakao.project.product.PopularProductInfo

data class PopularProductListResponse(
    val products: List<PopularProductInfo>
)