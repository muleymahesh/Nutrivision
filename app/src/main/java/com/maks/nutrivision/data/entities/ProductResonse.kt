package com.maks.nutrivision.data.entities

data class ProductResonse(
    val data: List<Product>,
    val responseCode: Int,
    val result: String
)